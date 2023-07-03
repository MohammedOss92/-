package com.mymuslem.sarrawi

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymuslem.sarrawi.adapter.ZekerTypes_Adapter
import com.mymuslem.sarrawi.databinding.FragmentFirstBinding
import com.mymuslem.sarrawi.db.viewModel.ZekerViewModel
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val zekerViewModel: ZekerViewModel by lazy {
        ViewModelProvider(this,ZekerViewModel.AzkarViewModelFactory(requireActivity().application))[ZekerViewModel::class.java]
    }
    private val zekertypesAdapter by lazy {  ZekerTypes_Adapter(requireContext()/*isDark*/) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

        setUpRv()
        menu_item()
    }

    private fun menu_item() {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.first_frag_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when(menuItem.itemId){

                    R.id.action_theme -> {


                        val prefs = SharedPref(requireContext())
                        val isDark = prefs.getThemeStatePref()
                        prefs.saveThemeStatePref(!isDark)

                        if(isDark){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                        else{
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }
                    }

                }
                return true
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpRv() = zekerViewModel.viewModelScope.launch {



        zekerViewModel.getAllZekerTypes().observe(requireActivity()) { listTvShows ->
            //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
            zekertypesAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW

            if(binding.recyclerView.adapter == null){
                zekertypesAdapter.zekerTypes_list = listTvShows
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = zekertypesAdapter
            }


        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}