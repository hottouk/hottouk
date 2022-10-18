package com.example.aop_part3_chapter02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(
    val quotes: List<Quote>,
    var isNameRevealed: Boolean
) : RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTextView: TextView = itemView.findViewById(R.id.quote_textview)
        private val nameTextView: TextView = itemView.findViewById(R.id.name_textview)

        fun bind(quote: Quote, isNameRevealed: Boolean) {
            quoteTextView.text = quote.quote
            if(isNameRevealed){
                nameTextView.text = quote.name
                nameTextView.isVisible=true
            }else{
                nameTextView.isVisible =false
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_quote, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position],isNameRevealed)
    }

    override fun getItemCount() = quotes.size
}