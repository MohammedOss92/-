package com.mymuslem.sarrawi

import android.os.Bundle
import android.view.*
import android.widget.Toast
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
import com.mymuslem.sarrawi.models.FavoriteModel
import com.mymuslem.sarrawi.models.Letters
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




        setUpRv()
        menu_item()
        adapterOnClick()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun adapterOnClick() {

        zekertypesAdapter.onItemClick = { it: Letters, i: Int ->
            val fav = FavoriteModel(it.ID, it.Name)
            // check if item is favorite or not

            if (it.Fav == 0) {
                zekerViewModel.update_fav(it.ID!!, 1) // update favorite item state
                zekerViewModel.add_fav(fav) // add item to db
                Toast.makeText(requireContext(), "تم الاضافة الى المفضلة", Toast.LENGTH_SHORT).show()
                setUpRv()
                zekertypesAdapter.notifyDataSetChanged()
            } else {
                zekerViewModel.update_fav(it.ID!!, 0) // update favorite item state
                zekerViewModel.delete_fav(fav) // delete item from db
                Toast.makeText(requireContext(), "تم الحذف من المفضلة", Toast.LENGTH_SHORT).show()
                setUpRv()
                zekertypesAdapter.notifyDataSetChanged()
            }

        }




    }

    private fun setUpRv() = zekerViewModel.viewModelScope.launch {



        zekerViewModel.getAllZekerTypes().observe(requireActivity()) { listShows ->
            //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
            zekertypesAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW

            if(binding.recyclerView.adapter == null){
                zekertypesAdapter.zekerTypes_list = listShows
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