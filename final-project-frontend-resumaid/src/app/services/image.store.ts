import { ComponentStore } from "@ngrx/component-store"
import { ImgUrlSlice } from "../models/image"
import { Injectable } from "@angular/core"

const INIT_VALUE: ImgUrlSlice = {
    imgUrl: ""
}

@Injectable()
export class ImageStore extends ComponentStore<ImgUrlSlice> {

    constructor() {
        super(INIT_VALUE)
    }

    //REDUCER/UPDATER
    readonly saveImgUrl = this.updater<string>(
        (currStore: ImgUrlSlice, result: string) => {
            currStore.imgUrl = result
            return currStore
        }
    )

    //SELECTOR/QUERY
    readonly getSavedImgUrl = this.select<string>(
        (currStore: ImgUrlSlice) => {
            return currStore.imgUrl
        }
    )
}
