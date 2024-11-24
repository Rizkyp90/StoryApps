package com.example.storyapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapps.R
import com.example.storyapps.api.ListStoryItem

class StoryAdapter(
    private val onItemClickListener: (ListStoryItem) -> Unit
) : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(STORY_COMPARATOR) {

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val storyImage: ImageView = itemView.findViewById(R.id.story_image)
        private val storyName: TextView = itemView.findViewById(R.id.story_name)
        private val storyDescription: TextView = itemView.findViewById(R.id.story_description)

        fun bind(story: ListStoryItem?) {
            storyName.text = story?.name
            storyDescription.text = story?.description
            Glide.with(itemView.context).load(story?.photoUrl).into(storyImage)

            itemView.setOnClickListener {
                story?.let { onItemClickListener(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    companion object {
        val STORY_COMPARATOR = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
