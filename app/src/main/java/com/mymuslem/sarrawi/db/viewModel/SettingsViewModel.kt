package com.mymuslem.sarrawi.db.viewModel

import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    var fontSize: Int = 20 // قيمة افتراضية لحجم الخط
    var fontType: String = "Arial" // نوع الخط الافتراضي
}