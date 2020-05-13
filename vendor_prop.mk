# Audio
PRODUCT_PROPERTY_OVERRIDES +=  \
    af.fast_track_multiplier=1 \
    audio.deep_buffer.media=true \
    audio.offload.min.duration.secs=30 \
    audio.offload.video=true \
    audio.sys.noisy.broadcast.delay=600 \
    audio.sys.offload.pstimeout.secs=3 \
    persist.audio.fluence.speaker=true \
    persist.audio.fluence.voicecall=true \
    persist.audio.fluence.voicecomm=true \
    persist.audio.fluence.voicerec=false \
    persist.dirac.acs.controller=gef \
    persist.dirac.acs.ignore_error=1 \
    persist.dirac.acs.no_usermsg=true \
    persist.dirac.config=64 \
    persist.dirac.gef.exs.appt=0x00011130 \
    persist.dirac.gef.exs.did=29,49 \
    persist.dirac.gef.exs.mid=0x10012DE3 \
    persist.dirac.gef.ext.appt=0x00011130,0x00011134,0x00011136 \
    persist.dirac.gef.ext.did=10,20,29,49 \
    persist.dirac.gef.ext.mid=0x10012DE1 \
    persist.dirac.gef.ins.appt=0x00011130 \
    persist.dirac.gef.ins.did=19,134,150 \
    persist.dirac.gef.ins.mid=0x10012DE2 \
    persist.dirac.gef.int.appt=0x00011130,0x00011134,0x00011136 \
    persist.dirac.gef.int.did=15,19,134,150 \
    persist.dirac.gef.int.mid=0x10012DE0 \
    persist.dirac.path=/vendor/etc/dirac/ \
    persist.dirac.qem.oppo.syss=true \
    persist.vendor.audio.ambisonic.auto.profile=false \
    persist.vendor.audio.ambisonic.capture=false \
    persist.vendor.audio.avs.afe_api_version=2 \
    persist.vendor.audio.fluence.audiorec=false \
    persist.vendor.audio.fluence.speaker=true \
    persist.vendor.audio.fluence.tmic.enabled=false \
    persist.vendor.audio.fluence.voicecall=true \
    persist.vendor.audio.fluence.voicerec=false \
    persist.vendor.audio.hifi.int_codec=true \
    persist.vendor.audio.ras.enabled=false \
    persist.vendor.audio.spv3.enable=true \
    persist.vendor.bt.a2dp_offload_cap=sbc-aptx-aptxhd-aac-ldac \
    persist.vendor.bt.aac_frm_ctl.enabled=true \
    vendor.audio.apptype.multirec.enabled=false \
    vendor.audio.dolby.ds2.enabled=false \
    vendor.audio.dolby.ds2.hardbypass=false \
    vendor.audio.feature.a2dp_offload.enable=true \
    vendor.audio.feature.afe_proxy.enable=true \
    vendor.audio.feature.anc_headset.enable=true \
    vendor.audio.feature.audiozoom.enable=false \
    vendor.audio.feature.battery_listener.enable=false \
    vendor.audio.feature.compr_cap.enable=false \
    vendor.audio.feature.compr_voip.enable=false \
    vendor.audio.feature.compress_in.enable=false \
    vendor.audio.feature.compress_meta_data.enable=true \
    vendor.audio.feature.concurrent_capture.enable=false \
    vendor.audio.feature.custom_stereo.enable=true \
    vendor.audio.feature.deepbuffer_as_primary.enable=false \
    vendor.audio.feature.display_port.enable=true \
    vendor.audio.feature.dsm_feedback.enable=false \
    vendor.audio.feature.dynamic_ecns.enable=false \
    vendor.audio.feature.ext_hw_plugin.enable=false \
    vendor.audio.feature.external_dsp.enable=false \
    vendor.audio.feature.external_speaker.enable=false \
    vendor.audio.feature.external_speaker_tfa.enable=false \
    vendor.audio.feature.fluence.enable=true \
    vendor.audio.feature.fm.enable=true \
    vendor.audio.feature.hdmi_edid.enable=true \
    vendor.audio.feature.hdmi_passthrough.enable=true \
    vendor.audio.feature.hfp.enable=true \
    vendor.audio.feature.hifi_audio.enable=true \
    vendor.audio.feature.hwdep_cal.enable=false \
    vendor.audio.feature.incall_music.enable=false \
    vendor.audio.feature.keep_alive.enable=false \
    vendor.audio.feature.kpi_optimize.enable=true \
    vendor.audio.feature.maxx_audio.enable=false \
    vendor.audio.feature.multi_voice_session.enable=true \
    vendor.audio.feature.ras.enable=true \
    vendor.audio.feature.record_play_concurency.enable=false \
    vendor.audio.feature.snd_mon.enable=true \
    vendor.audio.feature.spkr_prot.enable=true \
    vendor.audio.feature.src_trkn.enable=true \
    vendor.audio.feature.ssrec.enable=true \
    vendor.audio.feature.usb_offload.enable=true \
    vendor.audio.feature.usb_offload_burst_mode.enable=false \
    vendor.audio.feature.usb_offload_sidetone_volume.enable=false \
    vendor.audio.feature.vbat.enable=true \
    vendor.audio.feature.wsa.enable=false \
    vendor.audio.flac.sw.decoder.24bit=true \
    vendor.audio.hw.aac.encoder=true \
    vendor.audio.keep_alive.disabled=false \
    vendor.audio.offload.buffer.size.kb=64 \
    vendor.audio.offload.gapless.enabled=true \
    vendor.audio.offload.multiaac.enable=true \
    vendor.audio.offload.multiple.enabled=false \
    vendor.audio.offload.passthrough=false \
    vendor.audio.offload.track.enable=true \
    vendor.audio.parser.ip.buffer.size=262144 \
    vendor.audio.record.multiple.enabled=false \
    vendor.audio.safx.pbe.enabled=false \
    vendor.audio.tunnel.encode=false \
    vendor.audio.use.sw.alac.decoder=true \
    vendor.audio.use.sw.ape.decoder=true \
    vendor.audio.volume.headset.gain.depcal=true \
    vendor.audio_hal.period_size=192 \
    ro.qc.sdk.audio.fluencetype=none \
    ro.qc.sdk.audio.ssr=false \
    ro.vendor.audio.sdk.fluencetype=none \
    ro.vendor.audio.sdk.ssr=false \
    ro.dirac.acs.storeSettings=1

# Bluetooth
PRODUCT_PROPERTY_OVERRIDES +=  \
    vendor.qcom.bluetooth.soc=cherokee \
    persist.vendor.qcom.bluetooth.a2dp_offload_cap=sbc-aptx-aptxtws-aptxhd-aac-ldac \
    persist.vendor.qcom.bluetooth.aac_frm_ctl.enabled=true \
    persist.vendor.qcom.bluetooth.enable.splita2dp=true \
    persist.vendor.qcom.bluetooth.twsp_state.enabled=false \
    persist.bluetooth.a2dp_offload.cap=sbc-aac-aptx-aptxhd-ldac \
    persist.bluetooth.a2dp_offload.disabled=false \
    ro.vendor.bluetooth.wipower=false \
    ro.bluetooth.a2dp_offload.supported=true \
    persist.bluetooth.disableabsvol=false \
    persist.vendor.service.bt.iot.enablelogging=true \
    persist.vendor.bluetooth.a2dp.hal.implementation=true \
    persist.vendor.bt.a2dp.hal.implementation=true \
    persist.bluetooth.disableinbandringing=false \
    persist.bluetooth.specificatcmdsenable=true \
    persist.vendor.bluetooth.modem_nv_support=true \
    persist.vendor.service.bdroid.soc.alwayson=true \
    persist.vendor.service.bt.iotinfo.report.enable=true

# Camera
PRODUCT_PROPERTY_OVERRIDES +=  \
    ro.camera.hfr.enable=1 \
    ro.camera.relight.enable=0 \
    ro.camera.attr.detect.enable=1 \
    ro.camera.notify_nfc=1 \
    ro.camera.temperature.limit=470

# Codec2 (Enabling codec2.0 SW only for non-generic odm build variant)
#Rank OMX SW codecs lower than OMX HW codecs
PRODUCT_PROPERTY_OVERRIDES += \
    debug.stagefright.omx_default_rank.sw-audio=1 \
    debug.stagefright.omx_default_rank=0

# Dalvik VM configs
PRODUCT_PROPERTY_OVERRIDES +=  \
    dalvik.vm.heapgrowthlimit=384m \
    dalvik.vm.heapmaxfree=8m \
    dalvik.vm.heapminfree=512k \
    dalvik.vm.heapsize=512m \
    dalvik.vm.heapstartsize=16m \
    dalvik.vm.heaptargetutilization=0.75
    
# Display
PRODUCT_PROPERTY_OVERRIDES +=  \
    vendor.display.enable_default_color_mode=1 \
    ro.vendor.display.cabl=2 \
    persist.sys.enable.rc=1 \
    ro.display.rc.size=108,108,111,111 \
    ro.vendor.display.ad=1 \
    ro.vendor.display.sensortype=2 \
    ro.vendor.display.ad.sdr_calib_data=/vendor/etc/OPPO_OLED_AD_calib.cfg \
    ro.lcd.backlight.samsung_tenbit=10,517,103,303,355,457,563,676,805,883,1023 \
    ro.lcd.backlight.config_dsjm=11,958,13,334,517,794,1055,1325,1617,1770,1989,2047 \
    ro.vendor.display.backlightapp=1 \
    ro.display.underscreenfingerprint=1 \
    ro.display.underscreen.lightsensor.support=1 \
    ro.lcd.display.screen.underlightsensor.region=624,28,672,76 \
    ro.display.underscreen.lightsensor.screenshot.period=50 \
    persist.debug.wfd.enable=1 \
    ro.oppo.screen.heteromorphism=444,0:636,76

# FRP
PRODUCT_PROPERTY_OVERRIDES +=  \
    ro.frp.pst=/dev/block/bootdevice/by-name/frp

# Graphics
PRODUCT_PROPERTY_OVERRIDES +=  \
    debug.egl.hw=0 \
    debug.sf.hw=0 \
    drm.service.enabled=true \
    keyguard.no_require_sim=true \
    ro.hardware.vulkan=adreno \
    ro.opengles.version=196610 \
    ro.hardware.egl=adreno

# Keystore
PRODUCT_PROPERTY_OVERRIDES +=  \
    ro.hardware.keystore_desede=true

# Perf
PRODUCT_PROPERTY_OVERRIDES +=  \
    ro.vendor.extension_library=libqti-perfd-client.so

# Qcom system daemon
PRODUCT_PROPERTY_OVERRIDES +=  \
    persist.vendor.qcomsysd.enabled=1
   
# RIL
PRODUCT_PROPERTY_OVERRIDES +=  \
    persist.demo.hdmirotationlock=false \
    persist.radio.multisim.config=dsds \
    persist.vendor.radio.apm_sim_not_pwdn=1 \
    persist.vendor.radio.custom_ecc=1 \
    persist.vendor.radio.procedure_bytes=SKIP \
    persist.vendor.radio.rat_on=combine \
    persist.vendor.radio.sib16_support=1 \
    rild.libpath=/vendor/lib64/libril-qc-hal-qmi.so \
    ro.telephony.default_network=9,0 \
    persist.radio.multisim.config=dsds \
    persist.radio.custom_exp_ecc=1 \
    gsm.lte.ca.support=1 \
    ro.product.oem_dm=1 \
    persist.vendor.radio.process_sups_ind=1 \
    persist.vendor.radio.data_con_rprt=1

# Sensors
PRODUCT_PROPERTY_OVERRIDES +=  \
   persist.sys.oppo.fusionlight=true \
   persist.sys.oppo.proximity=true

# Surfaceflinger
PRODUCT_PROPERTY_OVERRIDES +=  \
    persist.sys.sf.color_saturation=1.0 \
    persist.sys.sf.native_mode=0 \
    debug.sf.disable_backpressure=1 \
    debug.sf.latch_unsignaled=1
    
PRODUCT_DEFAULT_PROPERTY_OVERRIDES += \
    ro.surface_flinger.has_wide_color_display=true \
    ro.surface_flinger.has_HDR_display=true \
    ro.surface_flinger.use_color_management=true \
    ro.surface_flinger.wcg_composition_dataspace=143261696

# Thermal
PRODUCT_PROPERTY_OVERRIDES +=  \
    persist.sys.enable.oscar=0 \
    persist.sys.oscar.dc=1

# VoWifi
PRODUCT_PROPERTY_OVERRIDES +=  \
    ro.telephony.iwlan_operation_mode=legacy \
    persist.data.iwlan.enable=true \
    persist.data.iwlan.rekey=4294967295

# Misc
PRODUCT_PROPERTY_OVERRIDES +=  \
    vendor.hw.fm.init=0 \
    ro.vendor.iocgrp.config=1 \
    vendor.power.pasr.enabled=true \
    vendor.voice.path.for.pcm.voip=true \
    qemu.hw.mainkeys=0 \
    persist.sys.enable.neo=1 \
    ro.af.client_heap_size_kbyte=7168 \
    ro.control_privapp_permissions=enforce
