# Automated CPDLC Station - EPWW

This project provides an automated CPDLC (Controller-Pilot Data Link Communications) station operating in the None network of the Hoppie system. It enables virtual pilots to interact with CPDLC services seamlessly, without requiring additional software installation or manual controller intervention.

## Features
- Fully automated CPDLC message processing
- No installation required – works seamlessly within the Hoppie network
- Supports a range of standard CPDLC requests, including logon, climb, descent, direct routing, and more
- Instant automated responses, ensuring uninterrupted flight operations

## Getting Started
To use this CPDLC station, you need a Hoppie ACARS account. If you don't have one, register here: [Hoppie Registration](https://www.hoppie.nl/acars/system/register.html).

You can find a full tutorial on [my YouTube channel](https://www.youtube.com/watch?v=6Zl-o9pIU-E)

### Station Details
- **Callsign:** EPWW
- **Hoppie Network:** None

### Supported Messages
The station supports the following CPDLC message types:

#### Logon Requests
*Automatically accepts logon requests.*
- **Request:** `REQUEST LOGON`
- **Response:** `LOGON ACCEPTED`

#### Direct Routing
*Approves direct-to waypoint requests.*
- **Request:** `REQUEST DIRECT TO SUBIX`
- **Response:** `PROCEED DIRECT TO @SUBIX@`

#### Altitude Changes
*Processes climb and descent requests, including requests tied to waypoints.*
- **Climb Request:** `REQUEST CLB TO FL180`
- **Response:** `CLIMB TO AND MAINTAIN @FL180@ REPORT LEVEL @FL180@`
- **Descent Request:** `REQUEST DES TO FL180`
- **Response:** `DESCENT TO AND MAINTAIN @FL180@ REPORT LEVEL @FL180@`

#### Heading and Ground Track Changes
*Handles turn requests for specific headings or ground tracks.*
- **Request:** `REQUEST HEADING 280`
- **Response:** `TURN @RIGHT@ HEADING @280@`

#### Speed Adjustments
*Supports requests to maintain a specific speed or Mach number.*
- **Request:** `REQUEST 250 KT`
- **Response:** `MAINTAIN @250@`
- **Mach Request:** `REQUEST M.82`
- **Response:** `MAINTAIN @M.82@`

#### Route Offsets
*Accepts offset requests due to weather or other operational needs.*
- **Request:** `REQUEST OFFSET 2 R OF ROUTE DUE TO WEATHER`
- **Response:** `OFFSET @2 R@ OF ROUTE`

## How It Works
1. Pilots send CPDLC requests via Hoppie’s network to EPWW.
2. The system automatically processes the request and generates an appropriate response.
3. The response is sent back to the aircraft, ensuring smooth and efficient communication.

## Contact
For questions, issues, or feature requests, feel free to reach out!
