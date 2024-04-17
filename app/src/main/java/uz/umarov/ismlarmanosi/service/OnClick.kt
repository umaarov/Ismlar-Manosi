package uz.umarov.ismlarmanosi.service

import uz.umarov.ismlarmanosi.models.Properties

interface OnCLick{
    fun click(properties: Properties, position: Int)
}