package com.nuevo.gameness.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.nuevo.gameness.R
import com.nuevo.gameness.ui.pages.MainActivity

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    val showLoading = MutableLiveData<Boolean>()
    private var loadingDialog: Dialog? = null

    abstract fun getViewBinding(): VB
    abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(findNavController().currentDestination?.id) {
            R.id.homeFragment -> R.id.menu_home
            R.id.announcementsFragment -> R.id.menu_announcements
            R.id.tournamentsFragment -> R.id.menu_tournaments
            R.id.eventsFragment -> R.id.menu_events
            R.id.profileFragment -> R.id.menu_profile
            else -> null
        }?.let { resId ->
            (requireActivity() as MainActivity).binding.bottomNavigationMain.menu.findItem(resId).isChecked = true
        }

        loadingDialog = Dialog(requireContext()).apply {
            setCancelable(false)
            setContentView(ProgressBar(context))
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        events()
        initView()
    }

    private fun events() {
        showLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) loadingDialog?.show()
            else loadingDialog?.cancel()
        }
    }

    fun setBackButton(visible: Boolean, onClickListener: (() -> Unit)? = null) {
        (requireActivity() as MainActivity).binding.apply {
            imageViewIcBack.visibility =
                if (visible) {
                    imageViewIcBack.setOnClickListener { onClickListener?.invoke() }
                    View.VISIBLE
                } else View.GONE
        }
    }

    fun setBottomVisibility(bottomLine: Boolean, bottomMenu: Boolean) {
        (requireActivity() as MainActivity).binding.apply {
            lineMain.visibility = if (bottomLine) View.VISIBLE else View.GONE
            bottomNavigationMain.visibility = if (bottomMenu) View.VISIBLE else View.GONE
        }
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun navigate(@IdRes resId: Int) {
        findNavController().navigate(resId)
    }

    fun navigate(@IdRes resId: Int, args: Bundle?) {
        findNavController().navigate(resId, args)
    }

    fun popBackStack() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        loadingDialog?.dismiss()
        loadingDialog = null
    }

}