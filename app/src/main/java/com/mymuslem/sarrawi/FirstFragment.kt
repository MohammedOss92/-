package com.mymuslem.sarrawi

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymuslem.sarrawi.adapter.ZekerTypes_Adapter
import com.mymuslem.sarrawi.databinding.FragmentFirstBinding
import com.mymuslem.sarrawi.db.viewModel.ZekerTypesViewModel
import com.mymuslem.sarrawi.models.FavoriteModel
import com.mymuslem.sarrawi.models.Letters
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    lateinit var _binding: FragmentFirstBinding
    private val binding get() = _binding!!

    private val zekerTypesViewModel: ZekerTypesViewModel by lazy {
        ViewModelProvider(this,ZekerTypesViewModel.AzkarViewModelFactory(requireActivity().application))[ZekerTypesViewModel::class.java]
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




        setUpRv()
        menu_item()
        adapterOnClick()
    }



    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun adapterOnClick() {

        zekertypesAdapter.onItemClick = {ID->
            val direction = FirstFragmentDirections.actionFirsFragmentToFragmentViewPager(ID)
                findNavController().navigate(direction)
            Log.d("MyApp", "$ID")
        }

        zekertypesAdapter.onFavClick = { it: Letters, i: Int ->
            val fav = FavoriteModel(it.ID!!, it.Name)
            // check if item is favorite or not

            if (it.Fav == 0) {
                zekerTypesViewModel.update_fav(it.ID!!, 1) // update favorite item state
                zekerTypesViewModel.add_fav(fav) // add item to db
                Toast.makeText(requireContext(), "تم الاضافة الى المفضلة", Toast.LENGTH_SHORT).show()
                setUpRv()

                zekertypesAdapter.notifyDataSetChanged()
            } else {
                zekerTypesViewModel.update_fav(it.ID!!, 0) // update favorite item state
                zekerTypesViewModel.delete_fav(fav) // delete item from db
                Toast.makeText(requireContext(), "تم الحذف من المفضلة", Toast.LENGTH_SHORT).show()
                setUpRv()
                zekertypesAdapter.notifyDataSetChanged()
            }

        }




    }

    private fun setUpRv() = zekerTypesViewModel.viewModelScope.launch {



        zekerTypesViewModel.getAllZekerTypes().observe(requireActivity()) { listShows ->
            //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
            zekertypesAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            zekertypesAdapter.zekerTypes_list = listShows
            if(binding.recyclerView.adapter == null){

                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = zekertypesAdapter
                zekertypesAdapter.notifyDataSetChanged()
            }else{
                zekertypesAdapter.notifyDataSetChanged()
            }


        }

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
}