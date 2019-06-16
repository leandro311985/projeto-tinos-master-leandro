package br.com.iresult.gmfmobile.model.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity
data class OcorrenciaLeitura(
        @PrimaryKey(autoGenerate = true) val uuid: Long,
        val numeroLigacao: Long,
        val ocorrencia: Ocorrencia,
        val imagensOcorrencia: List<File?>?
)