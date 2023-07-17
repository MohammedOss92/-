package com.mymuslem.sarrawi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mymuslem.sarrawi.adapter.FavoriteAdapter
import com.mymuslem.sarrawi.databinding.FragmentFavoriteBinding
import com.mymuslem.sarrawi.db.viewModel.ZekerTypesViewModel
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding

    private val favoriteAdapter by lazy { FavoriteAdapter(requireContext()) }

    private val zekerTypesViewModel: ZekerTypesViewModel by lazy {
        ViewModelProvider(this,
            ZekerTypesViewModel.AzkarViewModelFactory(requireActivity().application))[ZekerTypesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        (activity as MainActivity).fragment = 1

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRv()
        adapterOnClick()
    }


    private fun adapterOnClick() {

        favoriteAdapter.onItemClick = {
            zekerTypesViewModel.viewModelScope.launch {
                zekerTypesViewModel.update_fav(it.ID!!,0) // update item state
                zekerTypesViewModel.delete_fav(it)
//                val result = mainRepository.deleteFav(it)   // delete favorite item from db
                Toast.makeText(requireContext(),"تم الحذف من المفضلة", Toast.LENGTH_SHORT).show()
                setUpRv()
                favoriteAdapter.notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    private  fun setUpRv() = zekerTypesViewModel.viewModelScope.launch {

//        binding.rcMsgTypes.apply {
//            adapter = msgstypesAdapter
//            setHasFixedSize(true)
//        }


        zekerTypesViewModel.getFav().observe(viewLifecycleOwner) { listShows ->
            //  msgsAdapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            favoriteAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
//            msgsAdapter.msgsModel = listShows
//            binding.rcMsgs.adapter = msgsAdapter
            favoriteAdapter.zeker_fav_list = listShows
            if(binding.recyclerViewFav.adapter == null){
                binding.recyclerViewFav.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerViewFav.adapter = favoriteAdapter
                favoriteAdapter.notifyDataSetChanged()
            }else{
                favoriteAdapter.notifyDataSetChanged()
            }
            Log.e("tessst","enter111")

        }
    }


}