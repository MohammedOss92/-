package com.mymuslem.sarrawi

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.mymuslem.sarrawi.adapter.ZekerTypes_Adapter
import com.mymuslem.sarrawi.db.viewModel.SettingsViewModel


class FragmentSetting : Fragment() {
//    private val zekertypesAdapter by lazy {  ZekerTypes_Adapter(requireContext(), this/*isDark*/) }
//    lateinit var tvSeekBarValue:TextView
//    lateinit var tv_Size:TextView
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putInt("tvSeekBarValue", tvSeekBarValue.text.toString().toInt())
//    }
//
//    // في الاستعادة
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // ... إعادة بناء النشاط وتحديث الواجهة ...
//        var settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
//        if (savedInstanceState != null) {
//            val savedValue = savedInstanceState.getInt("tvSeekBarValue")
//            tvSeekBarValue.text = savedValue.toString()
//            settingsViewModel.fontSize = savedValue
//            tv_Size.textSize = settingsViewModel.fontSize.toFloat()
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        var rootview = inflater.inflate(R.layout.fragment_setting, container, false)
//
//        tvSeekBarValue=rootview.findViewById(R.id.tvSeekBarValue)
//        var settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
//        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
//        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        tv_Size=rootview.findViewById(R.id.fontSize)
//        val fontSizeSeekBar: SeekBar = rootview.findViewById(R.id.fontSizeSeekBar)
//        val fontSize = sharedPref.getInt("font_size", 14)
//        val fontSizetvSeekBarValue = sharedPref.getInt("tvSeekBarValue", 14)
//        fontSizeSeekBar.progress = fontSize.coerceIn(12, 30)
//        fontSizeSeekBar.progress = fontSize
//        fontSizeSeekBar.progress = fontSizetvSeekBarValue
//        fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//
//                // تحديث TextView لعرض القيمة المحددة من المؤشر
//                tvSeekBarValue.text = progress.toString()
//                settingsViewModel.fontSize = progress
//
//                tv_Size.textSize =  settingsViewModel.fontSize.toFloat()
//
//                sharedPref.edit().putInt("font_size", progress).apply()
//                // حفظ قيمة المؤشر SeekBar في SharedPreferences
//                val editor = sharedPref.edit()
//                editor.putInt("font_size", progress)
//                editor.putInt("tvSeekBarValue", progress)
//                editor.apply()
//
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//        })
//
//
//
//
//            return rootview
//    }

    private lateinit var tvSeekBarValue: TextView
    private lateinit var tv_Size: TextView
    private lateinit var settingsViewModel: SettingsViewModel
    private val sharedPref by lazy { requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_setting, container, false)

        tvSeekBarValue = rootview.findViewById(R.id.tvSeekBarValue)
        tv_Size = rootview.findViewById(R.id.fontSize)
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        val fontSizeSeekBar: SeekBar = rootview.findViewById(R.id.fontSizeSeekBar)
        val fontSize = sharedPref.getInt("font_size", 14)
        val fontSizetvSeekBarValue = sharedPref.getInt("tvSeekBarValue", 14)
        fontSizeSeekBar.progress = fontSize.coerceIn(12, 40)

        if (savedInstanceState != null) {
            val savedValue = savedInstanceState.getInt("tvSeekBarValue")
            tvSeekBarValue.text = savedValue.toString()
            settingsViewModel.fontSize = savedValue
            tv_Size.textSize = settingsViewModel.fontSize.toFloat()
        } else {
            tvSeekBarValue.text = fontSizetvSeekBarValue.toString()
            settingsViewModel.fontSize = fontSizetvSeekBarValue
            tv_Size.textSize = settingsViewModel.fontSize.toFloat()
        }

        fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // تحديث TextView لعرض القيمة المحددة من المؤشر
//                tvSeekBarValue.text = progress.toString()
//                settingsViewModel.fontSize = progress
//                tv_Size.textSize = settingsViewModel.fontSize.toFloat()
//
//                // حفظ قيمة المؤشر SeekBar و tvSeekBarValue في SharedPreferences
//                with(sharedPref.edit()) {
//                    putInt("font_size", progress)
//                    putInt("tvSeekBarValue", progress)
//                    apply()
//                }
                val newValue = progress.coerceIn(12, 40)
                tvSeekBarValue.text = newValue.toString()
                settingsViewModel.fontSize = newValue
                tv_Size.textSize = settingsViewModel.fontSize.toFloat()

                // حفظ قيمة المؤشر SeekBar و tvSeekBarValue في SharedPreferences
                with(sharedPref.edit()) {
                    putInt("font_size", newValue)
                    putInt("tvSeekBarValue", newValue)
                    apply()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        return rootview
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ... أي عمليات أخرى قد تكون هنا ...

        if (!::settingsViewModel.isInitialized) {
            settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
            settingsViewModel.fontSize = 14.coerceIn(12,30) // قيمة افتراضية لـ tvSeekBarValue
        }
    }


}