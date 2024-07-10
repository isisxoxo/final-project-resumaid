export interface BookingSlice {
    booking: any
}

export interface Booking {
    id: string
    userid: string
    starttime: DateTime
    endtime: DateTime
    meetinglink: string
}

export interface DateTime {
    value: EpochTimeStamp
    dateOnly: boolean
    timeZoneShift: number
}