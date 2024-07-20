package com.example.kotlinfinalproject.ui

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinfinalproject.R
import com.example.kotlinfinalproject.databinding.AllItemsLayoutBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class AllItemsFragment : Fragment() {

    private var _binding: AllItemsLayoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemsViewModel by activityViewModels()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AllItemsLayoutBinding.inflate(inflater, container, false)
        binding.addItemBtn.setOnClickListener {
            findNavController().navigate(R.id.action_allItemsFragment_to_addItemFragment)
        }

        drawerLayout = binding.root.findViewById(R.id.drawerLayout)
        navView = binding.root.findViewById(R.id.nav_view)

        // Set up the navigation icon click listener
        val toolbar: MaterialToolbar = binding.root.findViewById(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Set up navigation view item selected listener
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    Toast.makeText(requireContext(), "Profile clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_favorites -> {
                    Toast.makeText(requireContext(), "Favorites clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_Settings -> {
                    Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_share -> {
                    Toast.makeText(requireContext(), "Share clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_call -> {
                    Toast.makeText(requireContext(), "Emergency Call clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.items?.observe(viewLifecycleOwner) { items ->
            binding.recycler.adapter = ItemAdapter(items, object : ItemAdapter.ItemListener {
                override fun onItemLongClicked(id: Int) {
                    // Find the item by its ID
                    val item = items.find { it.id == id }
                    if (item != null) {
                        // Set the item in the ViewModel
                        viewModel.setItem(item)
                        // Pass the item ID to EditItemFragment
                        findNavController().navigate(R.id.action_allItemsFragment_to_editItemFragment,
                            bundleOf("itemId" to id)
                        )
                    }
                }

                override fun onItemClicked(index: Int) {
                    // Set the item in the ViewModel and navigate to the detail fragment
                    viewModel.setItem(items[index])
                    findNavController().navigate(R.id.action_allItemsFragment_to_detailItemFragment)
                }
            })
        }



    binding.recycler.layoutManager = LinearLayoutManager(requireContext())

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
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = (binding.recycler.adapter as ItemAdapter).itemAt(viewHolder.adapterPosition)
                viewModel.deleteItem(item)
                (binding.recycler.adapter as ItemAdapter).notifyItemRemoved(viewHolder.adapterPosition)
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


                // Draw the red background
                val background = RectF(itemView.left.toFloat()+borderSizePx, itemView.top.toFloat()+borderSizePx, itemView.right.toFloat(), itemView.bottom.toFloat()-borderSizePx)
                val paint = Paint().apply {
                    color = if (isCurrentlyActive) swipeBackgroundColor else Color.TRANSPARENT
                }
                c.drawRect(background, paint)

                // Draw the trash icon
                if (trashIcon != null) {
                    val iconLeft = itemView.right - trashIcon.intrinsicWidth - 100
                    val iconRight = itemView.right - 100
                    val iconTop = itemView.top + (itemView.height - trashIcon.intrinsicHeight) / 2
                    val iconBottom = iconTop + trashIcon.intrinsicHeight

                    trashIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    trashIcon.draw(c)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }).attachToRecyclerView(binding.recycler)

        // Set up the toolbar
        setupToolbar()
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
                R.id.search -> {
                    Toast.makeText(requireContext(), "Search clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete all items?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAll()
                Toast.makeText(requireContext(), "Items deleted", Toast.LENGTH_SHORT).show()
                refreshItems()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun refreshItems() {
        (binding.recycler.adapter as ItemAdapter).notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
