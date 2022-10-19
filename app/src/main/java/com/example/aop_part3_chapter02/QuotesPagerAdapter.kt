package com.example.aop_part3_chapter02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(
    val quotes: List<Quote>
) : RecyclerView.Adapter<QuotesPagerAdapter.QuoteItemViewHolder>(
) {
    inner class QuoteItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quoteTextView: TextView
        val nameTextView: TextView

        init {
            quoteTextView = itemView.findViewById<TextView>(R.id.quote_textview)
            nameTextView = itemView.findViewById<TextView>(R.id.name_textview)
        }

        fun bindView(quote: Quote) {
            quoteTextView.text = quote.quote
            nameTextView.text = quote.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_quote, parent, false)
        return QuoteItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: QuoteItemViewHolder, position: Int) {
        val quote: Quote = quotes[position]
        holder.bindView(quote)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }
}
