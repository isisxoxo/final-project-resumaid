import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { ResumeSlice } from "../models/resume";

const INIT_VALUE: ResumeSlice = {
    resumeForm: {}
}

@Injectable()
export class ResumeStore extends ComponentStore<ResumeSlice> {

    constructor() {
        super(INIT_VALUE)
    }

    //REDUCER/UPDATER
    readonly saveResume = this.updater<any>(
        (currStore: ResumeSlice, result: any) => {
            currStore.resumeForm = result
            return currStore
        }
    )

    //SELECTOR/QUERY
    readonly getSavedResume = this.select<any>(
        (currStore: ResumeSlice) => {
            return currStore.resumeForm
        }
    )
}
