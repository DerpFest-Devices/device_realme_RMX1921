#! /vendor/bin/sh

# Copyright (c) 2012-2013, 2016-2020, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above copyright
#       notice, this list of conditions and the following disclaimer in the
#       documentation and/or other materials provided with the distribution.
#     * Neither the name of The Linux Foundation nor
#       the names of its contributors may be used to endorse or promote
#       products derived from this software without specific prior written
#       permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NON-INFRINGEMENT ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
# CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
# EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
# PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
# OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
# OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
# ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#

function configure_zram_parameters() {
    MemTotalStr=`cat /proc/meminfo | grep MemTotal`
    MemTotal=${MemTotalStr:16:8}

    low_ram=`getprop ro.config.low_ram`

    # Zram disk - 75% for Go devices.
    # For 512MB Go device, size = 384MB, set same for Non-Go.
    # For 1GB Go device, size = 768MB, set same for Non-Go.
    # For 2GB Go device, size = 1536MB, set same for Non-Go.
    # For >2GB Non-Go devices, size = 50% of RAM size. Limit the size to 4GB.
    # And enable lz4 zram compression for Go targets.

    let RamSizeGB="( $MemTotal / 1048576 ) + 1"
    diskSizeUnit=M
    if [ $RamSizeGB -le 2 ]; then
        let zRamSizeMB="( $RamSizeGB * 1024 ) * 3 / 4"
    else
        let zRamSizeMB="( $RamSizeGB * 1024 ) / 2"
    fi

    # use MB avoid 32 bit overflow
    if [ $zRamSizeMB -gt 4096 ]; then
        let zRamSizeMB=4096
    fi

    echo lz4 > /sys/block/zram0/comp_algorithm

    if [ -f /sys/block/zram0/disksize ]; then
        if [ -f /sys/block/zram0/use_dedup ]; then
            echo 1 > /sys/block/zram0/use_dedup
        fi
        echo "$zRamSizeMB""$diskSizeUnit" > /sys/block/zram0/disksize

        # ZRAM may use more memory than it saves if SLAB_STORE_USER
        # debug option is enabled.
        if [ -e /sys/kernel/slab/zs_handle ]; then
            echo 0 > /sys/kernel/slab/zs_handle/store_user
        fi
        if [ -e /sys/kernel/slab/zspage ]; then
            echo 0 > /sys/kernel/slab/zspage/store_user
        fi

        mkswap /dev/block/zram0
        swapon /dev/block/zram0 -p 32758
    fi
}

#ifdef VENDOR_EDIT
#/*Huacai.Zhou@Tech.Kernel.MM, add oppo zram opt*/
function oppo_configure_zram_parameters() {
    MemTotalStr=`cat /proc/meminfo | grep MemTotal`
    MemTotal=${MemTotalStr:16:8}

    echo lz4 > /sys/block/zram0/comp_algorithm
    echo 160 > /proc/sys/vm/swappiness
    echo 60 > /proc/sys/vm/direct_swappiness
    echo 0 > /proc/sys/vm/page-cluster

    if [ -f /sys/block/zram0/disksize ]; then
        if [ -f /sys/block/zram0/use_dedup ]; then
            echo 1 > /sys/block/zram0/use_dedup
        fi

        if [ $MemTotal -le 524288 ]; then
            #config 384MB zramsize with ramsize 512MB
            echo 402653184 > /sys/block/zram0/disksize
        elif [ $MemTotal -le 1048576 ]; then
            #config 768MB zramsize with ramsize 1GB
            echo 805306368 > /sys/block/zram0/disksize
        elif [ $MemTotal -le 2097152 ]; then
            #config 1GB+256MB zramsize with ramsize 2GB
            echo lz4 > /sys/block/zram0/comp_algorithm
            echo 1342177280 > /sys/block/zram0/disksize
        elif [ $MemTotal -le 3145728 ]; then
            #config 1.9GB zramsize with ramsize 3GB
            echo 2040109466 > /sys/block/zram0/disksize
            #config 680M almk threshold with ramsize 3GB
            echo 174080 > /sys/module/lowmemorykiller/parameters/almk_totalram_threshold_pages
        elif [ $MemTotal -le 4194304 ]; then
            #config 2.5GB zram size with memory 4 GB
            echo 2684354560 > /sys/block/zram0/disksize
            #config 800M almk threshold with ramsize 4GB
            echo 204800 > /sys/module/lowmemorykiller/parameters/almk_totalram_threshold_pages
        elif [ $MemTotal -le 6291456 ]; then
            #config 3GB zram size with memory 6 GB
            echo 3221225472 > /sys/block/zram0/disksize
            #config 1G almk threshold with ramsize 6GB
            echo 262144 > /sys/module/lowmemorykiller/parameters/almk_totalram_threshold_pages
        else
            #config 4GB zram size with memory greater than 6GB
            echo 4294967296 > /sys/block/zram0/disksize
            #config 1.2G almk threshold with memory greater than 6GB
            echo 314572 > /sys/module/lowmemorykiller/parameters/almk_totalram_threshold_pages
        fi
        mkswap /dev/block/zram0
        swapon /dev/block/zram0 -p 32758
    fi
}
#endif /*VENDOR_EDIT*/

function configure_read_ahead_kb_values() {
    echo 512 > /sys/block/mmcblk0/bdi/read_ahead_kb
    echo 512 > /sys/block/mmcblk0rpmb/bdi/read_ahead_kb

    dmpts=$(ls /sys/block/*/queue/read_ahead_kb | grep -e dm -e mmc)
    for dm in $dmpts; do
        echo 512 > $dm
    done
}

function enable_swap() {
    MemTotalStr=`cat /proc/meminfo | grep MemTotal`
    MemTotal=${MemTotalStr:16:8}

    SWAP_ENABLE_THRESHOLD=1048576
    swap_enable=`getprop ro.vendor.qti.config.swap`

    # Enable swap initially only for 1 GB targets
    if [ "$MemTotal" -le "$SWAP_ENABLE_THRESHOLD" ] && [ "$swap_enable" == "true" ]; then
        # Static swiftness
        echo 1 > /proc/sys/vm/swap_ratio_enable
        echo 70 > /proc/sys/vm/swap_ratio

        # Swap disk - 200MB size
        if [ ! -f /data/vendor/swap/swapfile ]; then
            dd if=/dev/zero of=/data/vendor/swap/swapfile bs=1m count=200
        fi
        mkswap /data/vendor/swap/swapfile
        swapon /data/vendor/swap/swapfile -p 32758
    fi
}

function configure_memory_parameters() {
    # Set Memory parameters.
    #
    # Set per_process_reclaim tuning parameters
    # All targets will use vmpressure range 50-70,
    # All targets will use 512 pages swap size.
    #
    # Set allocstall_threshold to 0 for all targets.
    #

    # Set PPR parameters
    echo 6 > /sys/module/process_reclaim/parameters/min_score_adj
    echo 0 > /sys/module/process_reclaim/parameters/enable_process_reclaim
    echo 50 > /sys/module/process_reclaim/parameters/pressure_min
    echo 70 > /sys/module/process_reclaim/parameters/pressure_max
    echo 30 > /sys/module/process_reclaim/parameters/swap_opt_eff
    echo 512 > /sys/module/process_reclaim/parameters/per_swap_size

    # Set allocstall_threshold to 0 for all targets.
    # Set swappiness to 100 for all targets
    echo 0 > /sys/module/vmpressure/parameters/allocstall_threshold
    echo 100 > /proc/sys/vm/swappiness

    # Disable wsf for all targets beacause we are using efk.
    # wsf Range : 1..1000 So set to bare minimum value 1.
    echo 1 > /proc/sys/vm/watermark_scale_factor

#ifdef VENDOR_EDIT
#/*Huacai.Zhou@Tech.Kernel.MM, add oppo zram opt*/
	oppo_configure_zram_parameters
#else
    #configure_zram_parameters
#endif /*VENDOR_EDIT*/

    configure_read_ahead_kb_values

    enable_swap
fi
}

        #Apply settings for sdm710
        # Set the default IRQ affinity to the silver cluster. When a
        # CPU is isolated/hotplugged, the IRQ affinity is adjusted
        # to one of the CPU from the default IRQ affinity mask.
        echo 3f > /proc/irq/default_smp_affinity

      # Core control parameters on silver
      echo 0 0 0 0 1 1 > /sys/devices/system/cpu/cpu0/core_ctl/not_preferred
      echo 4 > /sys/devices/system/cpu/cpu0/core_ctl/min_cpus
      echo 60 > /sys/devices/system/cpu/cpu0/core_ctl/busy_up_thres
      echo 40 > /sys/devices/system/cpu/cpu0/core_ctl/busy_down_thres
      echo 100 > /sys/devices/system/cpu/cpu0/core_ctl/offline_delay_ms
      echo 0 > /sys/devices/system/cpu/cpu0/core_ctl/is_big_cluster
      echo 8 > /sys/devices/system/cpu/cpu0/core_ctl/task_thres

      # Setting b.L scheduler parameters
      echo 96 > /proc/sys/kernel/sched_upmigrate
      echo 90 > /proc/sys/kernel/sched_downmigrate
      echo 140 > /proc/sys/kernel/sched_group_upmigrate
      echo 120 > /proc/sys/kernel/sched_group_downmigrate
      echo 1 > /proc/sys/kernel/sched_walt_rotate_big_tasks

      # configure governor settings for little cluster
      echo "schedutil" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
      echo 0 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/rate_limit_us
      echo 1209600 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/hispeed_freq
      echo 576000 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq

      # configure governor settings for big cluster
      echo "schedutil" > /sys/devices/system/cpu/cpu6/cpufreq/scaling_governor
      echo 0 > /sys/devices/system/cpu/cpu6/cpufreq/schedutil/rate_limit_us
      echo 1344000 > /sys/devices/system/cpu/cpu6/cpufreq/schedutil/hispeed_freq
      echo 652800 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_min_freq

      # sched_load_boost as -6 is equivalent to target load as 85. It is per cpu tunable.
      echo -6 >  /sys/devices/system/cpu/cpu6/sched_load_boost
      echo -6 >  /sys/devices/system/cpu/cpu7/sched_load_boost
      echo 85 > /sys/devices/system/cpu/cpu6/cpufreq/schedutil/hispeed_load

      echo "0:1209600" > /sys/module/cpu_boost/parameters/input_boost_freq
      echo 40 > /sys/module/cpu_boost/parameters/input_boost_ms

      # Set Memory parameters
      configure_memory_parameters

      # Enable bus-dcvs
      for cpubw in /sys/class/devfreq/*qcom,cpubw*
            do
                echo "bw_hwmon" > $cpubw/governor
                echo 50 > $cpubw/polling_interval
                echo "1144 1720 2086 2929 3879 5931 6881" > $cpubw/bw_hwmon/mbps_zones
                echo 4 > $cpubw/bw_hwmon/sample_ms
                echo 68 > $cpubw/bw_hwmon/io_percent
                echo 20 > $cpubw/bw_hwmon/hist_memory
                echo 0 > $cpubw/bw_hwmon/hyst_length
                echo 80 > $cpubw/bw_hwmon/down_thres
                echo 0 > $cpubw/bw_hwmon/guard_band_mbps
                echo 250 > $cpubw/bw_hwmon/up_scale
                echo 1600 > $cpubw/bw_hwmon/idle_mbps
            done

            echo "cpufreq" > /sys/class/devfreq/soc:qcom,mincpubw/governor

            # Disable CPU Retention
            echo N > /sys/module/lpm_levels/L3/cpu0/ret/idle_enabled
            echo N > /sys/module/lpm_levels/L3/cpu1/ret/idle_enabled
            echo N > /sys/module/lpm_levels/L3/cpu2/ret/idle_enabled
            echo N > /sys/module/lpm_levels/L3/cpu3/ret/idle_enabled
            echo N > /sys/module/lpm_levels/L3/cpu4/ret/idle_enabled
            echo N > /sys/module/lpm_levels/L3/cpu5/ret/idle_enabled
            echo N > /sys/module/lpm_levels/L3/cpu6/ret/idle_enabled
            echo N > /sys/module/lpm_levels/L3/cpu7/ret/idle_enabled

            # Turn off scheduler boost at the end
            echo 0 > /proc/sys/kernel/sched_boost

            # Turn on sleep modes.
            echo 0 > /sys/module/lpm_levels/parameters/sleep_disabled

emmc_boot=`getprop vendor.boot.emmc`
case "$emmc_boot"
    in "true")
        chown -h system /sys/devices/platform/rs300000a7.65536/force_sync
        chown -h system /sys/devices/platform/rs300000a7.65536/sync_sts
        chown -h system /sys/devices/platform/rs300100a7.65536/force_sync
        chown -h system /sys/devices/platform/rs300100a7.65536/sync_sts
    ;;
esac

# Post-setup services
setprop vendor.post_boot.parsed 1

# Let kernel know our image version/variant/crm_version
if [ -f /sys/devices/soc0/select_image ]; then
    image_version="10:"
    image_version+=`getprop ro.build.id`
    image_version+=":"
    image_version+=`getprop ro.build.version.incremental`
    image_variant=`getprop ro.product.name`
    image_variant+="-"
    image_variant+=`getprop ro.build.type`
    oem_version=`getprop ro.build.version.codename`
    echo 10 > /sys/devices/soc0/select_image
    echo $image_version > /sys/devices/soc0/image_version
    echo $image_variant > /sys/devices/soc0/image_variant
    echo $oem_version > /sys/devices/soc0/image_crm_version
fi

# Change console log level as per console config property
console_config=`getprop persist.vendor.console.silent.config`
case "$console_config" in
    "1")
        echo "Enable console config to $console_config"
        echo 0 > /proc/sys/kernel/printk
        ;;
    *)
        echo "Enable console config to $console_config"
        ;;
esac

# Parse misc partition path and set property
misc_link=$(ls -l /dev/block/bootdevice/by-name/misc)
real_path=${misc_link##*>}
setprop persist.vendor.mmi.misc_dev_path $real_path
