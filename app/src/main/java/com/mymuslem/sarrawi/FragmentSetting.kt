package com.mymuslem.sarrawi

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
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


class FragmentSetting : Fragment() , TypefaceChangeListener {


    private lateinit var tvSeekBarValue: TextView
    private lateinit var tv_Size: TextView
    private lateinit var settingsViewModel: SettingsViewModel
    lateinit var spinner:Spinner
    // قائمة تحتوي على أسماء الخطوط المتاحة
    private val fontNames = listOf("a", "ab", "ac")

    // قائمة تحتوي على مصفوفة Typeface تمثل الخطوط المحملة من الملف assets
    private val typefaces = mutableListOf<Typeface>()
    private val zekertypesAdapter by lazy {  ZekerTypes_Adapter(requireContext(), this/*isDark*/) }

    private lateinit var typefaceChangeListener: TypefaceChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            typefaceChangeListener = context as TypefaceChangeListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement TypefaceChangeListener")
        }
    }

    private val sharedPref by lazy { requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_setting, container, false)

        tvSeekBarValue = rootview.findViewById(R.id.tvSeekBarValue)
        tv_Size = rootview.findViewById(R.id.fontSize)
        spinner = rootview.findViewById(R.id.fontTypeSpinner)

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

        // قم بتهيئة Spinner باستخدام ArrayAdapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fontNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        var selectedFontName = sharedPref.getString("selected_font", "a")

        // استجابة لاختيار المستخدم لنوع الخط من Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedFontName = fontNames[position]

                // حفظ اسم الخط المحدد في SharedPreferences
                with(sharedPref.edit()) {
                    putString("selected_font", selectedFontName)
                    apply()
                }

                // قم بتحميل الخط المحدد من ملفات المجلد assets/fonts باستخدام اسم الخط
                val selectedTypeface = getTypefaceFromAssets(selectedFontName!!)
                settingsViewModel.setSelectedTypeface(selectedTypeface)
                zekertypesAdapter.setTypeface(selectedTypeface)

                // قم بتحديث قائمة البيانات في الـ RecyclerView بعد تغيير الخط
                zekertypesAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // لا يلزم تنفيذ شيء في هذه الحالة
            }
        }

        // قم بتحميل الخطوط من مجلد الملفات assets
        loadTypefaces()

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

    override fun onTypefaceChanged(typeface: Typeface) {
        zekertypesAdapter.setTypeface(typeface)

        // تحديث قيمة حجم الخط في SharedPreferences بناءً على الخط الجديد
        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val fontSize = sharedPref.getInt("font_size", 14)
        settingsViewModel.fontSize = fontSize
        zekertypesAdapter.notifyDataSetChanged()

        // تحديث الخط المحدد في Spinner
        val fontNames = listOf("a", "ab", "ac") // قائمة تحتوي على أسماء الخطوط المتاحة
        val selectedFontName = getTypefaceName(typeface) // احصل على اسم الخط المحدد
        val selectedFontIndex = fontNames.indexOf(selectedFontName) // احصل على مؤشر الخط المحدد
        spinner.setSelection(selectedFontIndex) // اختر الخط المحدد في الـ Spinner
    }

    private fun loadTypefaces() {
        val assetManager = requireContext().assets

        try {
            // قائمة تحتوي على أسماء الملفات الموجودة في مجلد assets/fonts
            val fontFiles = assetManager.list("fonts") ?: emptyArray()

            // قم بتكرار قائمة الملفات واستخراج أسماء الملفات (دون الامتدادات)
            val fontNames = fontFiles.mapNotNull { fileName ->
                if (fileName.endsWith(".otf")) {
                    fileName.substringBeforeLast(".")
                } else {
                    null
                }
            }

            // قم بطباعة عدد ملفات الخطوط
            Log.d("FontFiles", "Number of font files: ${fontNames.size}")

            // قم بالتحقق من وجود خطوط
            if (fontNames.isNotEmpty()) {
                for (fontName in fontNames.take(3)) {
                    val typeface = getTypefaceFromAssets(fontName)
                    typefaces.add(typeface)
                }

                // اختر المؤشر المناسب هنا حسب الخط الذي تريد تحميله أولاً
                val selectedFontIndex = 0
                val selectedFont = fontNames[selectedFontIndex]
                val typeface = getTypefaceFromAssets(selectedFont)
                zekertypesAdapter.setTypeface(typeface)

                // قم بتحديث قائمة البيانات في الـ RecyclerView بعد تغيير الخط
                zekertypesAdapter.notifyDataSetChanged()
            } else {
                // إشعار المستخدم بعدم وجود خطوط محملة
                Toast.makeText(requireContext(), "لا توجد خطوط محملة", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            // يمكنك التعامل مع أي خطأ هنا، مثلاً إذا لم يتم العثور على المجلد أو الملفات
            e.printStackTrace()
        }
    }




    private fun getTypefaceFromAssets(fontName: String): Typeface {
        val assetManager = requireContext().assets
        return try {
            Typeface.createFromAsset(assetManager, "fonts/${fontName.toLowerCase()}.otf")
        } catch (e: Exception) {
            Typeface.DEFAULT
        }
    }

    private fun getTypefaceName(typeface: Typeface): String {
        val fontNames = listOf("a", "ab", "ac") // قائمة تحتوي على أسماء الخطوط المتاحة
        val typefaceIndex = typefaces.indexOf(typeface)
        return if (typefaceIndex in 0 until fontNames.size) {
            fontNames[typefaceIndex]
        } else {
            ""
        }
    }
}