#!/system/bin/sh 
chown system:system /dev/homekey
chmod 0666 /dev/homekey
chown system:system /proc/fp_unlock/lcd_status
chmod 0666 /proc/fp_unlock/lcd_status
chown system:system /proc/fp_unlock/lcdoff_status
chmod 0666 /proc/fp_unlock/lcdoff_status
