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
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.mymuslem.sarrawi.adapter.TypefaceChangeListener
import com.mymuslem.sarrawi.adapter.ZekerTypes_Adapter
import com.mymuslem.sarrawi.db.viewModel.SettingsViewModel



class FragmentSetting : Fragment() {

    // Views
    private lateinit var tvSeekBarValue: TextView
    private lateinit var tv_Size: TextView
    private lateinit var tv_Type: TextView
    private lateinit var spFont: Spinner
    private lateinit var nightModeSwitch: Switch

    private val font = arrayOf(
        "الخط الافتراضي",
        "الخط الاول",
        "الخط الثاني",
        "الخط الثالث",
        "الخط الرابع",
        "الخط الخامس",
        "الخط السادس"
    )
    private var font1: Typeface? = null
    private var font2: Typeface? = null
    private var font3: Typeface? = null
    private var font4: Typeface? = null
    private var font5: Typeface? = null
    private var font6: Typeface? = null
    private var font7: Typeface? = null
    private var Ffont: Typeface? = null




    // ViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    // Shared Preferences
    private val sharedPref by lazy { requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_setting, container, false)

        val sh_pref = SharedPref(requireContext())

        // Initialize Views
        tvSeekBarValue = rootview.findViewById(R.id.tvSeekBarValue)
        tv_Size = rootview.findViewById(R.id.fontSize)
        spFont = rootview.findViewById(R.id.fontTypeSpinner)
        tv_Type = rootview.findViewById(R.id.fontTypeLabel)
        nightModeSwitch = rootview.findViewById(R.id.nightModeSwitch)
        nightModeSwitch.isChecked = sh_pref.getThemeStatePref()

        // Initialize ViewModel
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        // SeekBar and TextView initialization
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

        // SeekBar Listener
        fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val newValue = progress.coerceIn(12, 40)
                tvSeekBarValue.text = newValue.toString()
                settingsViewModel.fontSize = newValue
                tv_Size.textSize = settingsViewModel.fontSize.toFloat()

                // Save values to SharedPreferences
                with(sharedPref.edit()) {
                    putInt("font_size", newValue)
                    putInt("tvSeekBarValue", newValue)
                    apply()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Spinner initialization
        val aFont = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, font)
        aFont.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spFont.adapter = aFont

        spFont.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                saveFontSettings(position)
                val selectedFont = when (position) {
                    0 -> font1
                    1 -> font2
                    2 -> font3
                    3 -> font4
                    4 -> font5
                    5 -> font6
                    6 -> font7
                    else -> font1
                }
                tv_Type.typeface = selectedFont
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        // SeekBar and TextView initialization

        // Get the current font size and night mode state from SharedPreferences
        fontSizeSeekBar.progress = fontSize.coerceIn(12, 40)

        // Set the font size and typeface for TextView
        tvSeekBarValue.text = fontSizetvSeekBarValue.toString()
        settingsViewModel.fontSize = fontSizetvSeekBarValue
        tv_Size.textSize = settingsViewModel.fontSize.toFloat()

        nightModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                // حفظ حالة الوضع الليلي في SharedPref
                sh_pref.saveThemeStatePref(true)
                saveFontSettings(settingsViewModel.fontSize)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                // حفظ حالة الوضع الليلي في SharedPref
                sh_pref.saveThemeStatePref(false)
                saveFontSettings(settingsViewModel.fontSize)
            }
        }


        initFonts()
        specifyFont()

        // Restore the default value of Spinner when the screen is opened
        restoreFontSetting()

        return rootview
    }

    // Override the onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewModel
        if (!::settingsViewModel.isInitialized) {
            settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
            settingsViewModel.fontSize = 14.coerceIn(12, 30) // Default value for tvSeekBarValue
        }
    }

    // Save font settings to SharedPreferences
    private fun saveFontSettings(selectedFont: Int) {
        val spEditor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
        spEditor.putInt("font", selectedFont)
        spEditor.apply()
    }

    // Restore the default value of Spinner when the screen is opened
    private fun restoreFontSetting() {
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        spFont.setSelection(sp.getInt("font", 0))
    }

    private fun initFonts() {
        font1 = Typeface.createFromAsset(requireContext().assets, "fonts/a.otf")
        font2 = Typeface.createFromAsset(requireContext().assets, "fonts/ab.otf")
        font3 = Typeface.createFromAsset(requireContext().assets, "fonts/ac.otf")
        font4 = Typeface.createFromAsset(requireContext().assets, "fonts/ad.otf")
        font5 = Typeface.createFromAsset(requireContext().assets, "fonts/ae.otf")
        font6 = Typeface.createFromAsset(requireContext().assets, "fonts/af.otf")
        font7 = Typeface.createFromAsset(requireContext().assets, "fonts/ag.otf")
    }

    private fun specifyFont() {
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val fontIndex = sp.getInt("font", 0) // استخراج رقم الخط المحدد

        Ffont = when (fontIndex) {
            0 -> font1
            1 -> font2
            2 -> font3
            3 -> font4
            4 -> font5
            5 -> font6
            6 -> font7
            else -> font1
        }


        val editor = sp.edit()
        editor.putInt("font", fontIndex)
        editor.apply()
    }
}


//class FragmentSetting : Fragment()  {
//
//
//    private lateinit var tvSeekBarValue: TextView
//    private lateinit var tv_Size: TextView
//    private lateinit var settingsViewModel: SettingsViewModel
//
//    private val zekertypesAdapter by lazy {  ZekerTypes_Adapter(requireContext(), this/*isDark*/) }
//
//    private lateinit var spFont: Spinner
////    private lateinit var button: Button
//    private var theSelectedFontPosition = 0
//    private val font = arrayOf(
//        "الخط الافتراضي",
//        "الخط الاول",
//        "الخط الثاني",
//        "الخط الثالث",
//        "الخط الرابع",
//        "الخط الخامس",
//        "الخط السادس"
//    )
//    private val sharedPref by lazy { requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val rootview = inflater.inflate(R.layout.fragment_setting, container, false)
//
//        tvSeekBarValue = rootview.findViewById(R.id.tvSeekBarValue)
//        tv_Size = rootview.findViewById(R.id.fontSize)
//
//        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
//
//        val fontSizeSeekBar: SeekBar = rootview.findViewById(R.id.fontSizeSeekBar)
//        val fontSize = sharedPref.getInt("font_size", 14)
//        val fontSizetvSeekBarValue = sharedPref.getInt("tvSeekBarValue", 14)
//        fontSizeSeekBar.progress = fontSize.coerceIn(12, 40)
//
//        if (savedInstanceState != null) {
//            val savedValue = savedInstanceState.getInt("tvSeekBarValue")
//            tvSeekBarValue.text = savedValue.toString()
//            settingsViewModel.fontSize = savedValue
//            tv_Size.textSize = settingsViewModel.fontSize.toFloat()
//        } else {
//            tvSeekBarValue.text = fontSizetvSeekBarValue.toString()
//            settingsViewModel.fontSize = fontSizetvSeekBarValue
//            tv_Size.textSize = settingsViewModel.fontSize.toFloat()
//        }
//
//        fontSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                // تحديث TextView لعرض القيمة المحددة من المؤشر
////                tvSeekBarValue.text = progress.toString()
////                settingsViewModel.fontSize = progress
////                tv_Size.textSize = settingsViewModel.fontSize.toFloat()
////
////                // حفظ قيمة المؤشر SeekBar و tvSeekBarValue في SharedPreferences
////                with(sharedPref.edit()) {
////                    putInt("font_size", progress)
////                    putInt("tvSeekBarValue", progress)
////                    apply()
////                }
//                val newValue = progress.coerceIn(12, 40)
//                tvSeekBarValue.text = newValue.toString()
//                settingsViewModel.fontSize = newValue
//                tv_Size.textSize = settingsViewModel.fontSize.toFloat()
//
//                // حفظ قيمة المؤشر SeekBar و tvSeekBarValue في SharedPreferences
//                with(sharedPref.edit()) {
//                    putInt("font_size", newValue)
//                    putInt("tvSeekBarValue", newValue)
//                    apply()
//                }
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//        })
//
//        val aFont = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, font)
//        aFont.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        spFont = rootview.findViewById(R.id.fontTypeSpinner)
////        button = rootview.findViewById(R.id.button)
//
//        spFont.adapter = aFont
//
//        spFont.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                theSelectedFontPosition = position
//                saveFontSettings(theSelectedFontPosition)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // TODO Auto-generated method stub
//            }
//        }
//
////        button.setOnClickListener {
////            saveFontSettings(theSelectedFontPosition)
////
////        }
//
//        // تحديد القيمة الافتراضية للـ Spinner عند فتح الشاشة
//        Ffont()
//
//
//
//        return rootview
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // ... أي عمليات أخرى قد تكون هنا ...
//
//        if (!::settingsViewModel.isInitialized) {
//            settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
//            settingsViewModel.fontSize = 14.coerceIn(12,30) // قيمة افتراضية لـ tvSeekBarValue
//        }
//    }
//
//    private fun saveFontSettings(selectedFont: Int) {
//        val spEditor = PreferenceManager.getDefaultSharedPreferences(requireContext()).edit()
//        spEditor.putInt("font", selectedFont)
//        spEditor.apply()
//    }
//
//    private fun Ffont() {
//        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
//        spFont.setSelection(sp.getInt("font", 0))
//
//    }
//
//
//}