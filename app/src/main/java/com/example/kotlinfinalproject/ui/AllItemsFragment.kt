package com.example.kotlinfinalproject.ui


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.data.model.Item
import com.example.kotlinfinalproject.databinding.AllItemsLayoutBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView


class AllItemsFragment : Fragment() {

    private var _binding: AllItemsLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemsViewModel by activityViewModels()

    private lateinit var bottomNavView: BottomNavigationView

    private var isFavoritesTab = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AllItemsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupUI() {
        binding.addItemBtn.setOnClickListener {
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }


        bottomNavView = binding.root.findViewById(R.id.bottomNavigation)





        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    isFavoritesTab = false
                    loadAllItems()
                    true
                }
                R.id.favorites -> {
                    isFavoritesTab = true
                    loadFavorites()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {


        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            private val swipeBackgroundColor = ContextCompat.getColor(requireContext(), R.color.red)
            private val trashIcon = ContextCompat.getDrawable(requireContext(), R.drawable.delete)

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeFlag(
                ItemTouchHelper.ACTION_STATE_SWIPE,
                ItemTouchHelper.LEFT
            )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = (binding.recycler.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)
                viewModel.deleteItem(item)
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val borderSizePx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics
                ).toInt()

                val background = RectF(
                    itemView.left.toFloat() + borderSizePx,
                    itemView.top.toFloat() + borderSizePx,
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat() - borderSizePx
                )
                val paint = Paint().apply {
                    color = if (isCurrentlyActive) swipeBackgroundColor else Color.TRANSPARENT
                }
                c.drawRect(background, paint)

                trashIcon?.let {
                    val iconLeft = itemView.right - it.intrinsicWidth - 100
                    val iconRight = itemView.right - 100
                    val iconTop = itemView.top + (itemView.height - it.intrinsicHeight) / 2
                    val iconBottom = iconTop + it.intrinsicHeight
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    it.draw(c)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }).attachToRecyclerView(binding.recycler)
        setupToolbar()
    }

    private fun setupObservers() {
        viewModel.items?.observe(viewLifecycleOwner) { items ->
            if (!isFavoritesTab) {
                binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                binding.recycler.adapter = ItemAdapter(items, itemListener)
            }
        }

        viewModel.favoriteItems?.observe(viewLifecycleOwner) { favorites ->
            if (isFavoritesTab) {
                binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                binding.recycler.adapter = ItemAdapter(favorites, itemListener)
            }
        }
    }

    private val itemListener = object : ItemAdapter.ItemListener {
        override fun onItemLongClicked(id: Int) {
            val item = (if (isFavoritesTab) viewModel.favoriteItems?.value else viewModel.items?.value)?.find { it.id == id }
            item?.let {
                viewModel.setItem(it)
                findNavController().navigate(
                    R.id.action_allItemsFragment_to_editItemFragment,
                    bundleOf("itemId" to id)
                )
            }
        }

        override fun onItemClicked(index: Int) {
            val item = (if (isFavoritesTab) viewModel.favoriteItems?.value else viewModel.items?.value)?.get(index)
            item?.let {
                viewModel.setItem(it)
                findNavController().navigate(R.id.action_allItemsFragment_to_detailItemFragment)
            }
        }

        override fun onFavoriteClicked(item: Item) {
            viewModel.toggleFavorite(item)
        }
    }

    private fun loadAllItems() {
        viewModel.items?.observe(viewLifecycleOwner) { items ->
            binding.recycler.adapter?.let { adapter ->
                if (adapter is ItemAdapter) {
                    adapter.updateItems(items)
                }
            }
        }
    }

    private fun loadFavorites() {
        viewModel.favoriteItems?.observe(viewLifecycleOwner) { favorites ->
            binding.recycler.adapter?.let { adapter ->
                if (adapter is ItemAdapter) {
                    adapter.updateItems(favorites)
                }
            }
        }
    }



    private fun setupToolbar() {
        val toolbar: MaterialToolbar = binding.topAppBar
        toolbar.inflateMenu(R.menu.top_app_bar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete -> {
                    showDeleteConfirmationDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirm_delete))
            .setMessage(getString(R.string.text_delete))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteAll()
                Toast.makeText(requireContext(), getString(R.string.items_deleted), Toast.LENGTH_SHORT).show()
                // No need to call refreshItems; the observer will handle it
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
