package com.example.movielist.screens.movieDetail.credits

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.R
import com.example.movielist.databinding.ItemCreditBinding
import com.example.movielist.di.TMDB_IMG_URL
import com.squareup.picasso.Picasso

class CastViewHolder(view: View, private val mPicasso: Picasso): RecyclerView.ViewHolder(view) {

    val mBinding = ItemCreditBinding.bind(view)

    fun bind(castMember: Cast) {
        with(mBinding) {
            creditCharacter.text = castMember.character
            creditName.text = castMember.name
                mPicasso
                    .load(TMDB_IMG_URL + castMember.profilePath)
                    .placeholder(R.drawable.person_placeholder)
                    .resizeDimen(R.dimen.itemCredit_img_width, R.dimen.itemCredit_img_height)
                    .into(photo)
        }
    }
}