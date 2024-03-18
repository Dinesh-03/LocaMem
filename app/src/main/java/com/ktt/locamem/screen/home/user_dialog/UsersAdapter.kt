package com.ktt.locamem.screen.home.user_dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ktt.locamem.databinding.ItemUserBinding
import com.ktt.locamem.model.User

class UsersAdapter(
    private val context: Context,
    private var users: List<User>,
    private val userItemListener: UserItemListener
): RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    interface UserItemListener {
        fun onClickedUser(user: User)
    }

    fun updateUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    inner class UserViewHolder(
        private val userBinding: ItemUserBinding,
        private val userItemListener: UserItemListener
    ) :
        RecyclerView.ViewHolder(userBinding.root) {
        private lateinit var user: User

        init {
            userBinding.root.setOnClickListener {
                userItemListener.onClickedUser(user)
            }
        }

        fun bind(user: User) {
            this.user = user
            userBinding.userName.text = user.userName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding: ItemUserBinding =
            ItemUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserViewHolder(binding, userItemListener)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = holder.bind(users[position])

}