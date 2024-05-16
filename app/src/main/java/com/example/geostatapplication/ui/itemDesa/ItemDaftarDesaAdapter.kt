package com.example.geostatapplication.ui.itemDesa

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geostatapplication.api.response.FeaturesItem
import com.example.geostatapplication.databinding.ItemDaftarDesaBinding
import com.example.geostatapplication.helper.limitWords

class ItemDaftarDesaAdapter(
    private val context: Context,
    private var listDesa: List<FeaturesItem>,
    private var onClick: (desa: FeaturesItem) -> Unit
): RecyclerView.Adapter<ItemDaftarDesaAdapter.ItemDaftarDesaHolder>() {
    inner class ItemDaftarDesaHolder(internal val binding: ItemDaftarDesaBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDaftarDesaHolder {
        val binding = ItemDaftarDesaBinding.inflate(
            LayoutInflater.from(context), parent, false
        )
        return ItemDaftarDesaHolder(binding)
    }

    override fun getItemCount(): Int = listDesa.size

    override fun onBindViewHolder(holder: ItemDaftarDesaHolder, position: Int) {
        val dataDesa = listDesa[position]

        val namaDesa = dataDesa.properties?.nmdesa
        val alamat = dataDesa.properties?.nmsls
        val jmlhKepalaKeluarga = dataDesa.properties?.kk

        holder.binding.tvNamaDesa.text = namaDesa
        holder.binding.tvAlamatDesa.text = limitWords(alamat.toString(), 8)
        holder.binding.tvJumlahKepalaKeluarga.text = jmlhKepalaKeluarga.toString()

        holder.binding.btnChoose.setOnClickListener {
            onClick(dataDesa)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataDesa(newDesaList: List<FeaturesItem?>?) {
        listDesa = newDesaList?.filterNotNull() ?: emptyList()
        notifyDataSetChanged()
    }

    fun searchDesa(keyword: String) {
        val filteredList = if (keyword.isNotBlank()) {
            listDesa.filter { it.properties?.nmdesa?.contains(keyword, ignoreCase = true) == true }
        } else {
            listDesa
        }
        updateDataDesa(filteredList)
    }

}