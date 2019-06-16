package br.com.iresult.gmfmobile.model.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PreenchimentoEstatistica(
        val roteiroNomeReg: String,
        @PrimaryKey val ruaNome: String,
        var ligacaoAtual: Ligacao?,
        var proximaLigacao: Ligacao?
)