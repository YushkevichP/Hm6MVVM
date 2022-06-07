package com.example.hm6_mvvm_koin.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.example.hm6_mvvm_koin.databinding.ItemPersonBinding
import com.example.hm6_mvvm_koin.model.CartoonPerson
import com.example.hm6_mvvm_koin.model.ItemType


// сюда нужно передать вторым параметром еще инфу по фото или АПИ  https://youtu.be/IDVxFjLeecA?t=10544
class PersonViewHolder(
    private val binding: ItemPersonBinding,
    private val onUserClicked: (ItemType<CartoonPerson>) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(person: ItemType<CartoonPerson>) {

        val newPerson = person as ItemType.Content

        with(binding) {

            imageView.load(newPerson.data.imageApi) {
                scale(Scale.FILL)
                size(ViewSizeResolver(root))
            }

            idPerson.text = newPerson.data.idApi.toString()
            textNameView.text = newPerson.data.nameApi
            root.setOnClickListener {
                onUserClicked(newPerson)
            }
        }

    }
}