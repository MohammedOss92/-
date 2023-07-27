package com.mymuslem.sarrawi

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.mymuslem.sarrawi.adapter.TypefaceChangeListener
import com.mymuslem.sarrawi.adapter.ZekerTypes_Adapter
import com.mymuslem.sarrawi.db.viewModel.SettingsViewModel


class FragmentSetting : Fragment()  {


    private lateinit var tvSeekBarValue: TextView
    private lateinit var tv_Size: TextView
    private lateinit var settingsViewModel: SettingsViewModel

    private val zekertypesAdapter by lazy {  ZekerTypes_Adapter(requireContext(), this/*isDark*/) }

    private lateinit var spFont: Spinner
//    private lateinit var button: Button
    private var theSelectedFontPosition = 0
    private val font = arrayOf(
        "الخط الافتراضي",
        "الخط الاول",
        "الخط الثاني",
        "الخط الثالث",
        "الخط الرابع",
        "الخط الخامس",
        "الخط السادس"
    )
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

        val aFont = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, font)

        spFont = rootview.findViewById(R.id.fontTypeSpinner)
//        button = rootview.findViewById(R.id.button)

        spFont.adapter = aFont

        spFont.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                theSelectedFontPosition = position
                saveFontSettings(theSelectedFontPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

//        button.setOnClickListener {
//            saveFontSettings(theSelectedFontPosition)
//
//        }

        // تحديد القيمة الافتراضية للـ Spinner عند فتح الشاشة
        Ffont()



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

    private fun saveFontSettings(selectedFont: Int) {
        val spEditor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
        spEditor.putInt("font", selectedFont)
        spEditor.apply()
    }

    private fun Ffont() {
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        spFont.setSelection(sp.getInt("font", 0))

    }


}