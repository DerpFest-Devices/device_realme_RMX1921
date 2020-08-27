### STK_ST2x2x Proximity (Infrared) sensor helper service
This repository contains source code implemented as a service to handle the events 
from Sensortek's stk_st2x2x proximity sensor on events like incall-ui and the other
possible cases where a fake panel blank is applied by the android framework and the
touchpanel proximity sensor (tp_proximity) stops sending event due to the suspended touchpanel caused by powermanager framework setting a non interactive state. 