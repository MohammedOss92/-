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
import com.mymuslem.sarrawi.db.viewModel.ZekerViewModel
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding!!

    private val favoriteAdapter by lazy { FavoriteAdapter(requireContext()) }

    private val zekerViewModel: ZekerViewModel by lazy {
        ViewModelProvider(this,
            ZekerViewModel.AzkarViewModelFactory(requireActivity().application))[ZekerViewModel::class.java]
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
            zekerViewModel.viewModelScope.launch {
                zekerViewModel.update_fav(it.ID!!,0) // update item state
                zekerViewModel.delete_fav(it)
//                val result = mainRepository.deleteFav(it)   // delete favorite item from db
                Toast.makeText(requireContext(),"تم الحذف من المفضلة", Toast.LENGTH_SHORT).show()
                setUpRv()
                favoriteAdapter.notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    private  fun setUpRv() = zekerViewModel.viewModelScope.launch {

//        binding.rcMsgTypes.apply {
//            adapter = msgstypesAdapter
//            setHasFixedSize(true)
//        }


        zekerViewModel.getFav().observe(viewLifecycleOwner) { listShows ->
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