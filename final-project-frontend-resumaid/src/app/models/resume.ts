export interface ResumeSlice {
    resumeForm: any
}

export interface education {

    eName: string
    eCountry: string
    eCert: string
    eFrom: Date
    eCurrent: boolean
    eTo: Date
    ePoints: string
}

export interface work {

    wName: string
    wCountry: string
    wRole: string
    wFrom: Date
    wCurrent: boolean
    wTo: Date
    wPoints: string
}

export interface cca {
    cName: string
    cCountry: string
    cTitle: string
    cFrom: Date
    cCurrent: boolean
    cTo: Date
    cPoints: string
}

export interface resume {

    id: string
    userId: string
    title: string
    fullName: string
    phone: string
    email: string
    education: education[]
    work: work[]
    cca: cca[]
    additional: string
    url: string
    creationTime: Date
    lastUpdateTime: Date
}
