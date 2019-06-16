package br.com.iresult.gmfmobile.model.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity
data class ServicoLeitura(
        @PrimaryKey(autoGenerate = true) val uuid: Long,
        val numeroLigacao: Long,
        val texto: String?,
        val rubrica: Rubrica,
        val imagens: List<File?>?
)