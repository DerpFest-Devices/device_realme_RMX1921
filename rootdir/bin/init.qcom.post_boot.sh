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

function 8953_sched_dcvs_eas()
{
    #governor settings
    echo 1 > /sys/devices/system/cpu/cpu0/online
    echo "schedutil" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
    echo 0 > /sys/devices/system/cpu/cpufreq/schedutil/up_rate_limit_us
    echo 0 > /sys/devices/system/cpu/cpufreq/schedutil/down_rate_limit_us
    #set the hispeed_freq
    echo 1401600 > /sys/devices/system/cpu/cpufreq/schedutil/hispeed_freq
    #default value for hispeed_load is 90, for 8953 and sdm450 it should be 85
    echo 85 > /sys/devices/system/cpu/cpufreq/schedutil/hispeed_load
}

function 8917_sched_dcvs_eas()
{
    #governor settings
    echo 1 > /sys/devices/system/cpu/cpu0/online
    echo "schedutil" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
    echo 0 > /sys/devices/system/cpu/cpufreq/schedutil/up_rate_limit_us
    echo 0 > /sys/devices/system/cpu/cpufreq/schedutil/down_rate_limit_us
    #set the hispeed_freq
    echo 1094400 > /sys/devices/system/cpu/cpufreq/schedutil/hispeed_freq
    #default value for hispeed_load is 90, for 8917 it should be 85
    echo 85 > /sys/devices/system/cpu/cpufreq/schedutil/hispeed_load
}

function 8937_sched_dcvs_eas()
{
    # enable governor for perf cluster
    echo 1 > /sys/devices/system/cpu/cpu0/online
    echo "schedutil" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
    echo 0 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/up_rate_limit_us
    echo 0 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/down_rate_limit_us
    #set the hispeed_freq
    echo 1094400 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/hispeed_freq
    #default value for hispeed_load is 90, for 8937 it should be 85
    echo 85 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/hispeed_load
    ## enable governor for power cluster
    echo 1 > /sys/devices/system/cpu/cpu4/online
    echo "schedutil" > /sys/devices/system/cpu/cpu4/cpufreq/scaling_governor
    echo 0 > /sys/devices/system/cpu/cpu4/cpufreq/schedutil/up_rate_limit_us
    echo 0 > /sys/devices/system/cpu/cpu4/cpufreq/schedutil/down_rate_limit_us
    #set the hispeed_freq
    echo 768000 > /sys/devices/system/cpu/cpu4/cpufreq/schedutil/hispeed_freq
    #default value for hispeed_load is 90, for 8937 it should be 85
    echo 85 > /sys/devices/system/cpu/cpu4/cpufreq/schedutil/hispeed_load

}

function configure_automotive_sku_parameters() {

    echo 1036800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq
    echo 1056000 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_min_freq
    echo 1171200 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_min_freq
    echo 1785600 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq
    echo 902400000  > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/min_freq
    echo 902400000  > /sys/class/devfreq/soc\:qcom,cpu4-cpu-l3-lat/min_freq
    echo 902400000  > /sys/class/devfreq/soc\:qcom,cpu7-cpu-l3-lat/min_freq
    echo 1612800000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/max_freq
    echo 1612800000 > /sys/class/devfreq/soc\:qcom,cpu4-cpu-l3-lat/max_freq
    echo 1612800000 > /sys/class/devfreq/soc\:qcom,cpu7-cpu-l3-lat/max_freq
#read feature id from nvram
reg_val=`cat /sys/devices/platform/soc/780130.qfprom/qfprom0/nvmem | od -An -t d4`
feature_id=$(((reg_val >> 20) & 0xFF))
log -t BOOT -p i "feature id '$feature_id'"
if [ $feature_id == 0 ]; then
       echo " SKU Configured : SA8155P"
       echo 2131200 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq
       echo 2419200 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_max_freq
       echo 0 > /sys/class/kgsl/kgsl-3d0/max_pwrlevel
elif [ $feature_id == 1 ]; then
        echo "SKU Configured : SA8150P"
        echo 1920000 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq
        echo 2227200 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_max_freq
        echo 3 > /sys/class/kgsl/kgsl-3d0/max_pwrlevel
else
        echo "unknown feature_id value" $feature_id
fi
}

function configure_sku_parameters() {

#read feature id from nvram
reg_val=`cat /sys/devices/platform/soc/780130.qfprom/qfprom0/nvmem | od -An -t d4`
feature_id=$(((reg_val >> 20) & 0xFF))
log -t BOOT -p i "feature id '$feature_id'"
if [ $feature_id == 6 ]; then
	echo " SKU Configured : SA6145"
	echo 748800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu5/cpufreq/scaling_min_freq
	echo 1017600 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_min_freq
	echo 1017600 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq
	echo 748800 > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq
	echo 748800 > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq
	echo 748800 > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq
	echo 748800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq
	echo 748800 > /sys/devices/system/cpu/cpu5/cpufreq/scaling_max_freq
	echo 1017600 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_max_freq
	echo 1017600 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_max_freq
	echo 940800000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/min_freq
	echo 1017600000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/max_freq
	echo 940800000 > /sys/class/devfreq/soc\:qcom,cpu6-cpu-l3-lat/min_freq
	echo 1017600000 > /sys/class/devfreq/soc\:qcom,cpu6-cpu-l3-lat/max_freq
	echo 3 > /sys/class/kgsl/kgsl-3d0/max_pwrlevel
	echo {class:ddr, res:capped, val: 1016} > /sys/kernel/debug/aop_send_message
	setprop vendor.sku_identified 1
	setprop vendor.sku_name "SA6145"
elif [ $feature_id == 5 ]; then
	echo "SKU Configured : SA6150"
	echo 748800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu5/cpufreq/scaling_min_freq
	echo 1017600 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_min_freq
	echo 1017600 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_min_freq
	echo 998400 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq
	echo 998400 > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq
	echo 998400 > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq
	echo 998400 > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq
	echo 998400 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq
	echo 998400 > /sys/devices/system/cpu/cpu5/cpufreq/scaling_max_freq
	echo 1708800 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_max_freq
	echo 1708800 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_max_freq
	echo 940800000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/min_freq
	echo 1363200000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/max_freq
	echo 940800000 > /sys/class/devfreq/soc\:qcom,cpu6-cpu-l3-lat/min_freq
	echo 1363200000 > /sys/class/devfreq/soc\:qcom,cpu6-cpu-l3-lat/max_freq
	echo 2 > /sys/class/kgsl/kgsl-3d0/max_pwrlevel
	echo {class:ddr, res:capped, val: 1333} > /sys/kernel/debug/aop_send_message
	setprop vendor.sku_identified 1
	setprop vendor.sku_name "SA6150"
elif [ $feature_id == 4 ] || [ $feature_id == 3 ]; then
	echo "SKU Configured : SA6155"
	echo 748800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu5/cpufreq/scaling_min_freq
	echo 1017600 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_min_freq
	echo 1017600 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_min_freq
	echo 1593600 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu5/cpufreq/scaling_max_freq
	echo 1900800 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_max_freq
	echo 1900800 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_max_freq
	echo 940800000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/min_freq
	echo 1363200000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/max_freq
	echo 940800000 > /sys/class/devfreq/soc\:qcom,cpu6-cpu-l3-lat/min_freq
	echo 1363200000 > /sys/class/devfreq/soc\:qcom,cpu6-cpu-l3-lat/max_freq
	echo 0 > /sys/class/kgsl/kgsl-3d0/max_pwrlevel
	echo {class:ddr, res:capped, val: 1555} > /sys/kernel/debug/aop_send_message
	setprop vendor.sku_identified 1
	setprop vendor.sku_name "SA6155"
else
	echo "SKU Configured : SA6155"
	echo 748800 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu1/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu2/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu3/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_min_freq
	echo 748800 > /sys/devices/system/cpu/cpu5/cpufreq/scaling_min_freq
	echo 1017600 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_min_freq
	echo 1017600 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_min_freq
	echo 1593600 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu1/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu2/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu3/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_max_freq
	echo 1593600 > /sys/devices/system/cpu/cpu5/cpufreq/scaling_max_freq
	echo 1900800 > /sys/devices/system/cpu/cpu6/cpufreq/scaling_max_freq
	echo 1900800 > /sys/devices/system/cpu/cpu7/cpufreq/scaling_max_freq
	echo 940800000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/min_freq
	echo 1363200000 > /sys/class/devfreq/soc\:qcom,cpu0-cpu-l3-lat/max_freq
	echo 940800000 > /sys/class/devfreq/soc\:qcom,cpu6-cpu-l3-lat/min_freq
	echo 1363200000 > /sys/class/devfreq/soc\:qcom,cpu6-cpu-l3-lat/max_freq
	echo 0 > /sys/class/kgsl/kgsl-3d0/max_pwrlevel
	echo {class:ddr, res:capped, val: 1555} > /sys/kernel/debug/aop_send_message
        setprop vendor.sku_identified 1
	setprop vendor.sku_name "SA6155"
fi
}

function 8953_sched_dcvs_hmp()
{
    #scheduler settings
    echo 3 > /proc/sys/kernel/sched_window_stats_policy
    echo 3 > /proc/sys/kernel/sched_ravg_hist_size
    #task packing settings
    echo 0 > /sys/devices/system/cpu/cpu0/sched_static_cpu_pwr_cost
    echo 0 > /sys/devices/system/cpu/cpu1/sched_static_cpu_pwr_cost
    echo 0 > /sys/devices/system/cpu/cpu2/sched_static_cpu_pwr_cost
    echo 0 > /sys/devices/system/cpu/cpu3/sched_static_cpu_pwr_cost
    echo 0 > /sys/devices/system/cpu/cpu4/sched_static_cpu_pwr_cost
    echo 0 > /sys/devices/system/cpu/cpu5/sched_static_cpu_pwr_cost
    echo 0 > /sys/devices/system/cpu/cpu6/sched_static_cpu_pwr_cost
    echo 0 > /sys/devices/system/cpu/cpu7/sched_static_cpu_pwr_cost
    # spill load is set to 100% by default in the kernel
    echo 3 > /proc/sys/kernel/sched_spill_nr_run
    # Apply inter-cluster load balancer restrictions
    echo 1 > /proc/sys/kernel/sched_restrict_cluster_spill
    # set sync wakee policy tunable
    echo 1 > /proc/sys/kernel/sched_prefer_sync_wakee_to_waker

    #governor settings
    echo 1 > /sys/devices/system/cpu/cpu0/online
    echo "interactive" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
    echo "19000 1401600:39000" > /sys/devices/system/cpu/cpufreq/interactive/above_hispeed_delay
    echo 85 > /sys/devices/system/cpu/cpufreq/interactive/go_hispeed_load
    echo 20000 > /sys/devices/system/cpu/cpufreq/interactive/timer_rate
    echo 1401600 > /sys/devices/system/cpu/cpufreq/interactive/hispeed_freq
    echo 0 > /sys/devices/system/cpu/cpufreq/interactive/io_is_busy
    echo "85 1401600:80" > /sys/devices/system/cpu/cpufreq/interactive/target_loads
    echo 39000 > /sys/devices/system/cpu/cpufreq/interactive/min_sample_time
    echo 40000 > /sys/devices/system/cpu/cpufreq/interactive/sampling_down_factor
    echo 19 > /proc/sys/kernel/sched_upmigrate_min_nice
    # Enable sched guided freq control
    echo 1 > /sys/devices/system/cpu/cpufreq/interactive/use_sched_load
    echo 1 > /sys/devices/system/cpu/cpufreq/interactive/use_migration_notif
    echo 200000 > /proc/sys/kernel/sched_freq_inc_notify
    echo 200000 > /proc/sys/kernel/sched_freq_dec_notify

}

function 8917_sched_dcvs_hmp()
{
    # HMP scheduler settings
    echo 3 > /proc/sys/kernel/sched_window_stats_policy
    echo 3 > /proc/sys/kernel/sched_ravg_hist_size
    echo 1 > /proc/sys/kernel/sched_restrict_tasks_spread
    # HMP Task packing settings
    echo 20 > /proc/sys/kernel/sched_small_task
    echo 30 > /sys/devices/system/cpu/cpu0/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu1/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu2/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu3/sched_mostly_idle_load

    echo 3 > /sys/devices/system/cpu/cpu0/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu1/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu2/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu3/sched_mostly_idle_nr_run

    echo 0 > /sys/devices/system/cpu/cpu0/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu1/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu2/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu3/sched_prefer_idle

    echo 1 > /sys/devices/system/cpu/cpu0/online
    echo "interactive" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
    echo "19000 1094400:39000" > /sys/devices/system/cpu/cpufreq/interactive/above_hispeed_delay
    echo 85 > /sys/devices/system/cpu/cpufreq/interactive/go_hispeed_load
    echo 20000 > /sys/devices/system/cpu/cpufreq/interactive/timer_rate
    echo 1094400 > /sys/devices/system/cpu/cpufreq/interactive/hispeed_freq
    echo 0 > /sys/devices/system/cpu/cpufreq/interactive/io_is_busy
    echo "1 960000:85 1094400:90" > /sys/devices/system/cpu/cpufreq/interactive/target_loads
    echo 40000 > /sys/devices/system/cpu/cpufreq/interactive/min_sample_time
    echo 40000 > /sys/devices/system/cpu/cpufreq/interactive/sampling_down_factor

    # Enable sched guided freq control
    echo 1 > /sys/devices/system/cpu/cpufreq/interactive/use_sched_load
    echo 1 > /sys/devices/system/cpu/cpufreq/interactive/use_migration_notif
    echo 50000 > /proc/sys/kernel/sched_freq_inc_notify
    echo 50000 > /proc/sys/kernel/sched_freq_dec_notify
}

function 8937_sched_dcvs_hmp()
{
    # HMP scheduler settings
    echo 3 > /proc/sys/kernel/sched_window_stats_policy
    echo 3 > /proc/sys/kernel/sched_ravg_hist_size
    # HMP Task packing settings
    echo 20 > /proc/sys/kernel/sched_small_task
    echo 30 > /sys/devices/system/cpu/cpu0/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu1/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu2/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu3/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu4/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu5/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu6/sched_mostly_idle_load
    echo 30 > /sys/devices/system/cpu/cpu7/sched_mostly_idle_load

    echo 3 > /sys/devices/system/cpu/cpu0/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu1/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu2/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu3/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu4/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu5/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu6/sched_mostly_idle_nr_run
    echo 3 > /sys/devices/system/cpu/cpu7/sched_mostly_idle_nr_run

    echo 0 > /sys/devices/system/cpu/cpu0/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu1/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu2/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu3/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu4/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu5/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu6/sched_prefer_idle
    echo 0 > /sys/devices/system/cpu/cpu7/sched_prefer_idle
    # enable governor for perf cluster
    echo 1 > /sys/devices/system/cpu/cpu0/online
    echo "interactive" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
    echo "19000 1094400:39000" > /sys/devices/system/cpu/cpu0/cpufreq/interactive/above_hispeed_delay
    echo 85 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/go_hispeed_load
    echo 20000 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/timer_rate
    echo 1094400 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/hispeed_freq
    echo 0 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/io_is_busy
    echo "1 960000:85 1094400:90 1344000:80" > /sys/devices/system/cpu/cpu0/cpufreq/interactive/target_loads
    echo 40000 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/min_sample_time
    echo 40000 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/sampling_down_factor

    # enable governor for power cluster
    echo 1 > /sys/devices/system/cpu/cpu4/online
    echo "interactive" > /sys/devices/system/cpu/cpu4/cpufreq/scaling_governor
    echo 39000 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/above_hispeed_delay
    echo 90 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/go_hispeed_load
    echo 20000 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/timer_rate
    echo 768000 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/hispeed_freq
    echo 0 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/io_is_busy
    echo "1 768000:90" > /sys/devices/system/cpu/cpu4/cpufreq/interactive/target_loads
    echo 40000 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/min_sample_time
    echo 40000 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/sampling_down_factor

    # Enable sched guided freq control
    echo 1 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/use_sched_load
    echo 1 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/use_migration_notif
    echo 1 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/use_sched_load
    echo 1 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/use_migration_notif
    echo 50000 > /proc/sys/kernel/sched_freq_inc_notify
    echo 50000 > /proc/sys/kernel/sched_freq_dec_notify

}

function sdm660_sched_interactive_dcvs() {

    echo 0 > /proc/sys/kernel/sched_select_prev_cpu_us
    echo 400000 > /proc/sys/kernel/sched_freq_inc_notify
    echo 400000 > /proc/sys/kernel/sched_freq_dec_notify
    echo 5 > /proc/sys/kernel/sched_spill_nr_run
    echo 1 > /proc/sys/kernel/sched_restrict_cluster_spill
    echo 100000 > /proc/sys/kernel/sched_short_burst_ns
    echo 1 > /proc/sys/kernel/sched_prefer_sync_wakee_to_waker
    echo 20 > /proc/sys/kernel/sched_small_wakee_task_load

    # disable thermal bcl hotplug to switch governor
    echo 0 > /sys/module/msm_thermal/core_control/enabled

    # online CPU0
    echo 1 > /sys/devices/system/cpu/cpu0/online
    # configure governor settings for little cluster
    echo "interactive" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
    echo 1 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/use_sched_load
    echo 1 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/use_migration_notif
    echo "19000 1401600:39000" > /sys/devices/system/cpu/cpu0/cpufreq/interactive/above_hispeed_delay
    echo 90 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/go_hispeed_load
    echo 20000 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/timer_rate
    echo 1401600 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/hispeed_freq
    echo 0 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/io_is_busy
    echo "85 1747200:95" > /sys/devices/system/cpu/cpu0/cpufreq/interactive/target_loads
    echo 39000 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/min_sample_time
    echo 0 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/max_freq_hysteresis
    echo 633600 > /sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq
    echo 1 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/ignore_hispeed_on_notif
    echo 1 > /sys/devices/system/cpu/cpu0/cpufreq/interactive/fast_ramp_down
    # online CPU4
    echo 1 > /sys/devices/system/cpu/cpu4/online
    # configure governor settings for big cluster
    echo "interactive" > /sys/devices/system/cpu/cpu4/cpufreq/scaling_governor
    echo 1 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/use_sched_load
    echo 1 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/use_migration_notif
    echo "19000 1401600:39000" > /sys/devices/system/cpu/cpu4/cpufreq/interactive/above_hispeed_delay
    echo 90 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/go_hispeed_load
    echo 20000 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/timer_rate
    echo 1401600 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/hispeed_freq
    echo 0 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/io_is_busy
    echo "85 1401600:90 2150400:95" > /sys/devices/system/cpu/cpu4/cpufreq/interactive/target_loads
    echo 39000 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/min_sample_time
    echo 59000 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/max_freq_hysteresis
    echo 1113600 > /sys/devices/system/cpu/cpu4/cpufreq/scaling_min_freq
    echo 1 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/ignore_hispeed_on_notif
    echo 1 > /sys/devices/system/cpu/cpu4/cpufreq/interactive/fast_ramp_down

    # bring all cores online
    echo 1 > /sys/devices/system/cpu/cpu0/online
    echo 1 > /sys/devices/system/cpu/cpu1/online
    echo 1 > /sys/devices/system/cpu/cpu2/online
    echo 1 > /sys/devices/system/cpu/cpu3/online
    echo 1 > /sys/devices/system/cpu/cpu4/online
    echo 1 > /sys/devices/system/cpu/cpu5/online
    echo 1 > /sys/devices/system/cpu/cpu6/online
    echo 1 > /sys/devices/system/cpu/cpu7/online

    # configure LPM
    echo N > /sys/module/lpm_levels/system/pwr/cpu0/ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/pwr/cpu1/ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/pwr/cpu2/ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/pwr/cpu3/ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/perf/cpu4/ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/perf/cpu5/ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/perf/cpu6/ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/perf/cpu7/ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/pwr/pwr-l2-dynret/idle_enabled
    echo N > /sys/module/lpm_levels/system/perf/perf-l2-dynret/idle_enabled
    echo N > /sys/module/lpm_levels/system/pwr/pwr-l2-ret/idle_enabled
    echo N > /sys/module/lpm_levels/system/perf/perf-l2-ret/idle_enabled

    # re-enable thermal and BCL hotplug
    echo 1 > /sys/module/msm_thermal/core_control/enabled

    # Enable bus-dcvs
    for cpubw in /sys/class/devfreq/*qcom,cpubw*
        do
            echo "bw_hwmon" > $cpubw/governor
            echo 50 > $cpubw/polling_interval
            echo 762 > $cpubw/min_freq
            echo "1525 3143 5859 7759 9887 10327 11863 13763" > $cpubw/bw_hwmon/mbps_zones
            echo 4 > $cpubw/bw_hwmon/sample_ms
            echo 85 > $cpubw/bw_hwmon/io_percent
            echo 100 > $cpubw/bw_hwmon/decay_rate
            echo 50 > $cpubw/bw_hwmon/bw_step
            echo 20 > $cpubw/bw_hwmon/hist_memory
            echo 0 > $cpubw/bw_hwmon/hyst_length
            echo 80 > $cpubw/bw_hwmon/down_thres
            echo 0 > $cpubw/bw_hwmon/low_power_ceil_mbps
            echo 34 > $cpubw/bw_hwmon/low_power_io_percent
            echo 20 > $cpubw/bw_hwmon/low_power_delay
            echo 0 > $cpubw/bw_hwmon/guard_band_mbps
            echo 250 > $cpubw/bw_hwmon/up_scale
            echo 1600 > $cpubw/bw_hwmon/idle_mbps
        done

    for memlat in /sys/class/devfreq/*qcom,memlat-cpu*
        do
            echo "mem_latency" > $memlat/governor
            echo 10 > $memlat/polling_interval
            echo 400 > $memlat/mem_latency/ratio_ceil
        done
    echo "cpufreq" > /sys/class/devfreq/soc:qcom,mincpubw/governor
}

function sdm660_sched_schedutil_dcvs() {

    if [ $KernelVersionA -ge 4 ] && [ $KernelVersionB -ge 19 ]; then
        # configure governor settings for little cluster
        echo "schedutil" > /sys/devices/system/cpu/cpufreq/policy0/scaling_governor
        echo 0 > /sys/devices/system/cpu/cpufreq/policy0/schedutil/up_rate_limit_us
        echo 0 > /sys/devices/system/cpu/cpufreq/policy0/schedutil/down_rate_limit_us
        echo 1401600 > /sys/devices/system/cpu/cpufreq/policy0/schedutil/hispeed_freq
        echo 0 > /sys/devices/system/cpu/cpufreq/policy0/schedutil/pl
        echo 633600 > /sys/devices/system/cpu/cpufreq/policy0/scaling_min_freq
        echo 902400 > /sys/devices/system/cpu/cpufreq/policy0/schedutil/rtg_boost_freq

        # configure governor settings for big cluster
        echo "schedutil" > /sys/devices/system/cpu/cpufreq/policy4/scaling_governor
        echo 0 > /sys/devices/system/cpu/cpufreq/policy4/schedutil/up_rate_limit_us
        echo 0 > /sys/devices/system/cpu/cpufreq/policy4/schedutil/down_rate_limit_us
        echo 1401600 > /sys/devices/system/cpu/cpufreq/policy4/schedutil/hispeed_freq
        echo 0 > /sys/devices/system/cpu/cpufreq/policy4/schedutil/pl
        echo 1113600 > /sys/devices/system/cpu/cpufreq/policy4/scaling_min_freq
        echo 0 > /sys/devices/system/cpu/cpufreq/policy4/schedutil/rtg_boost_freq
    else
        # configure governor settings for little cluster
        echo "schedutil" > /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor
        echo 0 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/up_rate_limit_us
        echo 0 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/down_rate_limit_us
        echo 1401600 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/hispeed_freq

        # configure governor settings for big cluster
        echo "schedutil" > /sys/devices/system/cpu/cpu4/cpufreq/scaling_governor
        echo 0 > /sys/devices/system/cpu/cpu4/cpufreq/schedutil/up_rate_limit_us
        echo 0 > /sys/devices/system/cpu/cpu4/cpufreq/schedutil/down_rate_limit_us
        echo 1401600 > /sys/devices/system/cpu/cpu4/cpufreq/schedutil/hispeed_freq
    fi

    echo 1 > /proc/sys/kernel/sched_walt_rotate_big_tasks

    #if the kernel version >=4.19,set input_boost_freq accordingly
    if [ $KernelVersionA -ge 4 ] && [ $KernelVersionB -ge 19 ]; then
        echo "0:1401600" > /sys/devices/system/cpu/cpu_boost/input_boost_freq
        echo 40 > /sys/devices/system/cpu/cpu_boost/input_boost_ms
    else
        echo "0:1401600" > /sys/module/cpu_boost/parameters/input_boost_freq
        echo 40 > /sys/module/cpu_boost/parameters/input_boost_ms
    fi

    # sched_load_boost as -6 is equivalent to target load as 85. It is per cpu tunable.
    echo -6 >  /sys/devices/system/cpu/cpu0/sched_load_boost
    echo -6 >  /sys/devices/system/cpu/cpu1/sched_load_boost
    echo -6 >  /sys/devices/system/cpu/cpu2/sched_load_boost
    echo -6 >  /sys/devices/system/cpu/cpu3/sched_load_boost
    echo -6 >  /sys/devices/system/cpu/cpu4/sched_load_boost
    echo -6 >  /sys/devices/system/cpu/cpu5/sched_load_boost
    echo -6 >  /sys/devices/system/cpu/cpu6/sched_load_boost
    echo -6 >  /sys/devices/system/cpu/cpu7/sched_load_boost
    echo 85 > /sys/devices/system/cpu/cpu0/cpufreq/schedutil/hispeed_load
    echo 85 > /sys/devices/system/cpu/cpu4/cpufreq/schedutil/hispeed_load

    # Enable bus-dcvs
    for device in /sys/devices/platform/soc
    do
        for cpubw in $device/*cpu-cpu-ddr-bw/devfreq/*cpu-cpu-ddr-bw
        do
            echo "bw_hwmon" > $cpubw/governor
            echo 50 > $cpubw/polling_interval
            echo 762 > $cpubw/min_freq
            echo "1525 3143 5859 7759 9887 10327 11863 13763" > $cpubw/bw_hwmon/mbps_zones
            echo 4 > $cpubw/bw_hwmon/sample_ms
            echo 85 > $cpubw/bw_hwmon/io_percent
            echo 100 > $cpubw/bw_hwmon/decay_rate
            echo 50 > $cpubw/bw_hwmon/bw_step
            echo 20 > $cpubw/bw_hwmon/hist_memory
            echo 0 > $cpubw/bw_hwmon/hyst_length
            echo 80 > $cpubw/bw_hwmon/down_thres
            echo 0 > $cpubw/bw_hwmon/guard_band_mbps
            echo 250 > $cpubw/bw_hwmon/up_scale
            echo 1600 > $cpubw/bw_hwmon/idle_mbps
        done

        if [ $KernelVersionA -ge 4 ] && [ $KernelVersionB -le 14 ]; then
            for memlat in $device/*cpu*-lat/devfreq/*cpu*-lat
            do
                echo "mem_latency" > $memlat/governor
                echo 10 > $memlat/polling_interval
                echo 400 > $memlat/mem_latency/ratio_ceil
            done

            for latfloor in $device/*cpu*-ddr-latfloor*/devfreq/*cpu-ddr-latfloor*
            do
                echo "compute" > $latfloor/governor
                echo 10 > $latfloor/polling_interval
            done
        fi
    done

    if [ $KernelVersionA -ge 4 ] && [ $KernelVersionB -ge 19 ]; then
        # memlat specific settings are moved to seperate file under
        # device/target specific folder
        setprop vendor.dcvs.prop 1
    fi

}

KernelVersionStr=`cat /proc/sys/kernel/osrelease`
KernelVersionS=${KernelVersionStr:2:2}
KernelVersionA=${KernelVersionStr:0:1}
KernelVersionB=${KernelVersionS%.*}

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
    MemTotalStr=`cat /proc/meminfo | grep MemTotal`
    MemTotal=${MemTotalStr:16:8}

    dmpts=$(ls /sys/block/*/queue/read_ahead_kb | grep -e dm -e mmc)

    # Set 128 for <= 3GB &
    # set 512 for >= 4GB targets.
    if [ $MemTotal -le 3145728 ]; then
        echo 128 > /sys/block/mmcblk0/bdi/read_ahead_kb
        echo 128 > /sys/block/mmcblk0rpmb/bdi/read_ahead_kb
        for dm in $dmpts; do
            echo 128 > $dm
        done
    else
        echo 512 > /sys/block/mmcblk0/bdi/read_ahead_kb
        echo 512 > /sys/block/mmcblk0rpmb/bdi/read_ahead_kb
        for dm in $dmpts; do
            echo 512 > $dm
        done
    fi
}

function disable_core_ctl() {
    if [ -f /sys/devices/system/cpu/cpu0/core_ctl/enable ]; then
        echo 0 > /sys/devices/system/cpu/cpu0/core_ctl/enable
    else
        echo 1 > /sys/devices/system/cpu/cpu0/core_ctl/disable
    fi
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
    # Set Low memory killer minfree parameters
    # 32 bit Non-Go, all memory configurations will use 15K series
    # 32 bit Go, all memory configurations will use uLMK + Memcg
    # 64 bit will use Google default LMK series.
    #
    # Set ALMK parameters (usually above the highest minfree values)
    # vmpressure_file_min threshold is always set slightly higher
    # than LMK minfree's last bin value for all targets. It is calculated as
    # vmpressure_file_min = (last bin - second last bin ) + last bin
    #
    # Set allocstall_threshold to 0 for all targets.
    #

ProductName=`getprop ro.product.name`
low_ram=`getprop ro.config.low_ram`

if [ "$ProductName" == "msmnile" ] || [ "$ProductName" == "kona" ] || [ "$ProductName" == "sdmshrike_au" ]; then
      # Enable ZRAM
#ifdef VENDOR_EDIT
#/*Huacai.Zhou@Tech.Kernel.MM, add oppo zram opt*/
       oppo_configure_zram_parameters
#else
#      configure_zram_parameters
#endif /*VENDOR_EDIT*/
      configure_read_ahead_kb_values
      echo 0 > /proc/sys/vm/page-cluster
#ifndef VENDOR_EDIT
#/*Huacai.Zhou@Tech.Kernel.MM, add oppo zram opt*/
#      echo 100 > /proc/sys/vm/swappiness
#endif /*VENDOR_EDIT*/
else
    arch_type=`uname -m`

    # Set parameters for 32-bit Go targets.
    if [ "$low_ram" == "true" ]; then
        # Disable KLMK, ALMK, PPR & Core Control for Go devices
        echo 0 > /sys/module/lowmemorykiller/parameters/enable_lmk
        echo 0 > /sys/module/lowmemorykiller/parameters/enable_adaptive_lmk
        echo 0 > /sys/module/process_reclaim/parameters/enable_process_reclaim
        disable_core_ctl
        # Enable oom_reaper for Go devices
        if [ -f /proc/sys/vm/reap_mem_on_sigkill ]; then
            echo 1 > /proc/sys/vm/reap_mem_on_sigkill
        fi
    else

        # Read adj series and set adj threshold for PPR and ALMK.
        # This is required since adj values change from framework to framework.
        adj_series=`cat /sys/module/lowmemorykiller/parameters/adj`
        adj_1="${adj_series#*,}"
        set_almk_ppr_adj="${adj_1%%,*}"

        # PPR and ALMK should not act on HOME adj and below.
        # Normalized ADJ for HOME is 6. Hence multiply by 6
        # ADJ score represented as INT in LMK params, actual score can be in decimal
        # Hence add 6 considering a worst case of 0.9 conversion to INT (0.9*6).
        # For uLMK + Memcg, this will be set as 6 since adj is zero.
        set_almk_ppr_adj=$(((set_almk_ppr_adj * 6) + 6))
        #ifdef VENDOR_EDIT
        #/*Kejun.Xu@ANDROID.PERFORMANCE, change ALMK adj from 606 to 701*/
        echo 701 > /sys/module/lowmemorykiller/parameters/adj_max_shift
        #else
        #echo $set_almk_ppr_adj > /sys/module/lowmemorykiller/parameters/adj_max_shift
        #endif /*VENDOR_EDIT*/

        # Calculate vmpressure_file_min as below & set for 64 bit:
        # vmpressure_file_min = last_lmk_bin + (last_lmk_bin - last_but_one_lmk_bin)
        if [ "$arch_type" == "aarch64" ]; then
            minfree_series=`cat /sys/module/lowmemorykiller/parameters/minfree`
            minfree_1="${minfree_series#*,}" ; rem_minfree_1="${minfree_1%%,*}"
            minfree_2="${minfree_1#*,}" ; rem_minfree_2="${minfree_2%%,*}"
            minfree_3="${minfree_2#*,}" ; rem_minfree_3="${minfree_3%%,*}"
            minfree_4="${minfree_3#*,}" ; rem_minfree_4="${minfree_4%%,*}"
            minfree_5="${minfree_4#*,}"

            vmpres_file_min=$((minfree_5 + (minfree_5 - rem_minfree_4)))
            echo $vmpres_file_min > /sys/module/lowmemorykiller/parameters/vmpressure_file_min
        else
            # Set LMK series, vmpressure_file_min for 32 bit non-go targets.
            # Disable Core Control, enable KLMK for non-go 8909.
            if [ "$ProductName" == "msm8909" ]; then
                disable_core_ctl
                echo 1 > /sys/module/lowmemorykiller/parameters/enable_lmk
            fi
        echo "15360,19200,23040,26880,34415,43737" > /sys/module/lowmemorykiller/parameters/minfree
        echo 53059 > /sys/module/lowmemorykiller/parameters/vmpressure_file_min
        fi

        # Enable adaptive LMK for all targets &
        # use Google default LMK series for all 64-bit targets >=2GB.
        echo 1 > /sys/module/lowmemorykiller/parameters/enable_adaptive_lmk

        # Enable oom_reaper
        if [ -f /sys/module/lowmemorykiller/parameters/oom_reaper ]; then
            echo 1 > /sys/module/lowmemorykiller/parameters/oom_reaper
        fi

        if [[ "$ProductName" != "bengal"* ]]; then
            #bengal has appcompaction enabled. So not needed
            # Set PPR parameters for other targets
            if [ -f /sys/devices/soc0/soc_id ]; then
                soc_id=`cat /sys/devices/soc0/soc_id`
            else
                soc_id=`cat /sys/devices/system/soc/soc0/id`
            fi

            case "$soc_id" in
              # Do not set PPR parameters for premium targets
              # sdm845 - 321, 341
              # msm8998 - 292, 319
              # msm8996 - 246, 291, 305, 312
              "321" | "341" | "292" | "319" | "246" | "291" | "305" | "312")
                ;;
              *)
                #Set PPR parameters for all other targets.
                echo $set_almk_ppr_adj > /sys/module/process_reclaim/parameters/min_score_adj
                echo 1 > /sys/module/process_reclaim/parameters/enable_process_reclaim
                echo 50 > /sys/module/process_reclaim/parameters/pressure_min
                echo 70 > /sys/module/process_reclaim/parameters/pressure_max
                echo 30 > /sys/module/process_reclaim/parameters/swap_opt_eff
                echo 512 > /sys/module/process_reclaim/parameters/per_swap_size
                ;;
            esac
        fi
    fi

    if [[ "$ProductName" == "bengal"* ]]; then
        #Set PPR nomap parameters for bengal targets
        echo 1 > /sys/module/process_reclaim/parameters/enable_process_reclaim
        echo 50 > /sys/module/process_reclaim/parameters/pressure_min
        echo 70 > /sys/module/process_reclaim/parameters/pressure_max
        echo 30 > /sys/module/process_reclaim/parameters/swap_opt_eff
        echo 0 > /sys/module/process_reclaim/parameters/per_swap_size
        echo 7680 > /sys/module/process_reclaim/parameters/tsk_nomap_swap_sz
    fi

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

function enable_memory_features()
{
    MemTotalStr=`cat /proc/meminfo | grep MemTotal`
    MemTotal=${MemTotalStr:16:8}

    if [ $MemTotal -le 2097152 ]; then
        #Enable B service adj transition for 2GB or less memory
        setprop ro.vendor.qti.sys.fw.bservice_enable true
        setprop ro.vendor.qti.sys.fw.bservice_limit 5
        setprop ro.vendor.qti.sys.fw.bservice_age 5000

        #Enable Delay Service Restart
        setprop ro.vendor.qti.am.reschedule_service true
    fi
}

function start_hbtp()
{
        # Start the Host based Touch processing but not in the power off mode.
#        bootmode=`getprop ro.bootmode`
#        if [ "charger" != $bootmode ]; then
#                start vendor.hbtp
#        fi
}

        #Apply settings for sdm710
        # Set the default IRQ affinity to the silver cluster. When a
        # CPU is isolated/hotplugged, the IRQ affinity is adjusted
        # to one of the CPU from the default IRQ affinity mask.
        echo 3f > /proc/irq/default_smp_affinity

        if [ -f /sys/devices/soc0/hw_platform ]; then
            hw_platform=`cat /sys/devices/soc0/hw_platform`
        else
            hw_platform=`cat /sys/devices/system/soc/soc0/hw_platform`
        fi

            # Start Host based Touch processing
            case "$hw_platform" in
              "MTP" | "Surf" | "RCM" | "QRD" )
                  start_hbtp
                  ;;
            esac

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

            #Enable mem_latency governor for DDR scaling
            for memlat in /sys/class/devfreq/*qcom,memlat-cpu*
            do
                echo "mem_latency" > $memlat/governor
                echo 10 > $memlat/polling_interval
                echo 400 > $memlat/mem_latency/ratio_ceil
            done

            #Enable mem_latency governor for L3 scaling
            for memlat in /sys/class/devfreq/*qcom,l3-cpu*
            do
                echo "mem_latency" > $memlat/governor
                echo 10 > $memlat/polling_interval
                echo 400 > $memlat/mem_latency/ratio_ceil
            done

            #Enable userspace governor for L3 cdsp nodes
            for l3cdsp in /sys/class/devfreq/*qcom,l3-cdsp*
            do
                echo "userspace" > $l3cdsp/governor
                chown -h system $l3cdsp/userspace/set_freq
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

            # cpuset parameters
            echo 0-5 > /dev/cpuset/background/cpus
            echo 0-5 > /dev/cpuset/system-background/cpus

            # Turn off scheduler boost at the end
            echo 0 > /proc/sys/kernel/sched_boost

            # Turn on sleep modes.
            echo 0 > /sys/module/lpm_levels/parameters/sleep_disabled

chown -h system /sys/devices/system/cpu/cpufreq/ondemand/sampling_rate
chown -h system /sys/devices/system/cpu/cpufreq/ondemand/sampling_down_factor
chown -h system /sys/devices/system/cpu/cpufreq/ondemand/io_is_busy

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
