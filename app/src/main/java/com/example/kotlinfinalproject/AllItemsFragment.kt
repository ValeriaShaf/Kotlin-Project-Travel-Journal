package com.example.kotlinfinalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.kotlinfinalproject.databinding.AllItemsLayoutBinding

class AllItemsFragment: Fragment() {

    private var _binding: AllItemsLayoutBinding?=null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding=AllItemsLayoutBinding.inflate(inflater,container,false)
        binding.addItemBtn.setOnClickListener{
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()


        binding.recycler.layoutManager=LinearLayoutManager(requireContext())

        ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            )= makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)


            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ItemManager.remove(viewHolder.adapterPosition)
                binding.recycler.adapter!!.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recycler)
    }

    override fun onResume() {
        super.onResume()
        refreshItems()
    }
//arguments?.getString("title")?.let {
//    Toast.makeText(requireActivity(),it,Toast.LENGTH_SHORT).show()
//}
    private fun setupRecyclerView() {
    binding.recycler.adapter = ItemAdapter(ItemManager.items, object : ItemAdapter.ItemListener {
        override fun onItemClicked(id: String) {
            findNavController().navigate(
                R.id.action_allItemsFragment_to_editItemFragment,
                bundleOf("itemId" to id)
            )
        }

        override fun onItemLongClicked(index: Int) {
            findNavController().navigate(
                R.id.action_allItemsFragment_to_detailItemFragment,
                bundleOf("item" to index)
            )
        }
    })
}

    private fun refreshItems() {
        (binding.recycler.adapter as ItemAdapter).notifyDataSetChanged()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}