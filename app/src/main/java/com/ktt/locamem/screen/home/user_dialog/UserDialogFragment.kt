package com.ktt.locamem.screen.home.user_dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktt.locamem.databinding.DialogFragmentUserBinding
import com.ktt.locamem.model.User
import com.ktt.locamem.screen.home.HomeScreenActivity
import com.ktt.locamem.screen.login.LoginScreenActivity
import com.ktt.locamem.util.Constants
import com.ktt.locamem.util.SharedPreferences


class UserDialogFragment: DialogFragment() {

    private lateinit var binding: DialogFragmentUserBinding
    private lateinit var userDialogViewModel: UserDialogViewModel
    private lateinit var usersAdapter: UsersAdapter
    private val userItemListener = object : UsersAdapter.UserItemListener {
        override fun onClickedUser(user: User) {
            dismiss()
            SharedPreferences.addData(requireActivity(), Constants.USER_PREFERENCE, Constants.USER_NAME, user.userName)
            requireActivity().startActivity(Intent(requireActivity(), HomeScreenActivity::class.java))
            requireActivity().finish()
        }
    }
    private lateinit var userName: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentUserBinding.inflate(layoutInflater)
        userDialogViewModel = ViewModelProvider(requireActivity())[UserDialogViewModel::class.java]
        usersAdapter = UsersAdapter(requireContext(), listOf(), userItemListener)
        binding.userRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.userRecycler.adapter = usersAdapter
        userName = SharedPreferences.getString(requireActivity(), Constants.USER_PREFERENCE, Constants.USER_NAME)
        userDialogViewModel.getUsers(userName)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userName = SharedPreferences.getString(requireActivity(), Constants.USER_PREFERENCE, Constants.USER_NAME)
        binding.userLayout.userName.text = userName
        userDialogViewModel.users.observe(requireActivity()) {
            if (it.isEmpty()){
                binding.noUserAvailable.visibility = View.VISIBLE
                binding.userRecycler.visibility = View.GONE
            } else {
                usersAdapter.updateUsers(it)
                binding.noUserAvailable.visibility = View.GONE
                binding.userRecycler.visibility = View.VISIBLE
            }
        }

        binding.logout.setOnClickListener {
            SharedPreferences.addData(requireActivity(), Constants.USER_PREFERENCE, Constants.USER_NAME, "")
            dismiss()
            startActivity(Intent(requireActivity(), LoginScreenActivity::class.java))
            requireActivity().finish()
        }

        binding.close.setOnClickListener {
            dismiss()
        }
    }

}