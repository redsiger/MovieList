package com.example.movielist.screens.movieDetail.credits

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.databinding.ItemCreditBinding
import com.example.movielist.di.TMDB_IMG_URL
import com.squareup.picasso.Picasso

class CrewViewHolder(view: View, private val mPicasso: Picasso): RecyclerView.ViewHolder(view) {

    val mBinding = ItemCreditBinding.bind(view)

    fun bind(crewMember: Crew) {
        with(mBinding) {
            creditCharacter.text = crewMember.job
            creditName.text = crewMember.name
            mPicasso
                .load(TMDB_IMG_URL + crewMember.profilePath)
                .placeholder(R.drawable.person_placeholder)
                .resizeDimen(R.dimen.itemCredit_img_width, R.dimen.itemCredit_img_height)
                .into(photo)
        }
    }
}