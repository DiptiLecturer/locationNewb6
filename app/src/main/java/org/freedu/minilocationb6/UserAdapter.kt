package org.freedu.minilocationb6

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.freedu.minilocationb6.databinding.ItemUserBinding

class UserAdapter(
    private val userList: ArrayList<User>,
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val user = userList[position]

        holder.binding.email.text = user.email
        holder.binding.lat.text = "Lat: ${user.latitude ?: "N/A"}"
        holder.binding.lng.text = "Long: ${user.longitude ?: "N/A"}"

        holder.itemView.setOnClickListener {
            onItemClick(user)
        }
    }

    override fun getItemCount(): Int = userList.size


    inner class UserViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)
}