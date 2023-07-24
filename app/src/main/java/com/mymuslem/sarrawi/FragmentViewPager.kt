package com.mymuslem.sarrawi

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.mymuslem.sarrawi.adapter.VPagerAdapter
import com.mymuslem.sarrawi.db.viewModel.SettingsViewModel
import com.mymuslem.sarrawi.db.viewModel.ZekerTypesViewModel
import com.mymuslem.sarrawi.db.viewModel.ZekerViewModel
import com.mymuslem.sarrawi.models.Zekr
import me.relex.circleindicator.CircleIndicator3


class FragmentViewPager : Fragment() {
    private var zeker_list = mutableListOf<Zekr>()
    var view_pager2: ViewPager2?=null
    private val zekerViewModel: ZekerViewModel by lazy {
        ViewModelProvider(this,
            ZekerViewModel.AzkarViewModelFactory(requireActivity().application))[ZekerViewModel::class.java]
    }

    private var argsId = -1
    private var argId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argsId= FragmentViewPagerArgs.fromBundle(requireArguments()).typeID
        argId=FragmentViewPagerArgs.fromBundle(requireArguments()).typeID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_view_pager, container, false)
        view_pager2 = rootView.findViewById(R.id.ss)
        val adapter = VPagerAdapter(zeker_list,this)
        val ind: CircleIndicator3 = rootView.findViewById(R.id.aa)

        view_pager2?.let {
            it.adapter = adapter
            it.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            ind.setViewPager(it)
            val currentPosition = view_pager2!!.currentItem ?: 0
            view_pager2!!.currentItem = currentPosition + 1
//            if (currentPosition != null) {
//                view_pager2!!.currentItem = currentPosition + 1
//            }
        }

        zekerViewModel.getAllZeker(argsId).observe(viewLifecycleOwner) { updatedZekerList ->
            zeker_list.clear()
            zeker_list.addAll(updatedZekerList)
            view_pager2?.setCurrentItem(zeker_list.size,false)
//            view_pager2?.currentItem = zeker_list.size
            ind.setViewPager(view_pager2)
//            ind.createIndicators(updatedZekerList.size)
            adapter.notifyDataSetChanged()
        }

        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        var settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        val fontSize = sharedPref.getInt("font_size", 14)
        settingsViewModel.fontSize = fontSize

        adapter.notifyDataSetChanged()

        return rootView
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}