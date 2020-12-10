package com.example.labo03

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labo03.beaconsUtilities.BeaconItem

// Class pour afficher la liste des Beacons avec une RecyclerView
class BeaconAdapter  : RecyclerView.Adapter<BeaconAdapter.BeaconViewHolder>() {
    // Création de la liste des Beacons
    private var beaconItems :MutableList<BeaconItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconViewHolder {
        return BeaconViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_listbeacon, parent, false))
    }

    override fun getItemCount(): Int {
        return beaconItems.size
    }

    override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
        holder.bind(beaconItems[position])
    }
    // Récupération des Beacons du scan pour les afficher
    fun submitItems(items: MutableList<BeaconItem>){
        beaconItems = items
    }

    class BeaconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Récupération de la vue spécifique à l'affichage d'un objet Beacon
        private val uuid: TextView = itemView.findViewById(R.id.UUID)
        private val majeurmineur: TextView = itemView.findViewById(R.id.majeurmineur)
        private val distance: TextView = itemView.findViewById(R.id.Distance)
        private val rssi: TextView = itemView.findViewById(R.id.RSSI)
        // Génération de l'affichage d'un objet Beacon
        fun bind(item: BeaconItem){
            uuid.text = item.UUID
            distance.text = item.distance
            rssi.text = item.RSSI
            val majminText = "${item.majeur} - ${item.mineur}"
            majeurmineur.text = majminText
        }
    }
}