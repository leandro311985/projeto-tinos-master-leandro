package br.com.iresult.gmfmobile.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import br.com.iresult.gmfmobile.R
import kotlinx.android.synthetic.main.dialog_entrega.*

class FormaEntregaDialog(context: Context) : GmfDialog(context) {

    private var selectedForma: FormaEntrega? = null
    private var saveListener: ((formaEntrega: FormaEntrega) -> Unit)? = null

    enum class FormaEntrega(val title: String) {
        FORMA_ENTREGA_MAOS("Em mãos"),
        FORMA_ENTREGA_CORREIOS("Caixa dos correios"),
        FORMA_ENTREGA_PORTA("Embaixo da porta"),
        FORMA_ENTREGA_PORTAO("No portão"),
        FORMA_ENTREGA_VIZINHO_ESQUERDA("Vizinho da esquerda"),
        FORMA_ENTREGA_VIZINHO_DIREITA("Vizinho da direita"),
        FORMA_ENTREGA_NAO_ENTREGUE("Não entregue")
    }

    override fun getLayoutResource(): Int = R.layout.dialog_entrega

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_maos.setOnClickListener {
            toggleSelectedOption(it, FormaEntrega.FORMA_ENTREGA_MAOS, R.drawable.ic_mao_white)
        }
        btn_correios.setOnClickListener {
            toggleSelectedOption(it, FormaEntrega.FORMA_ENTREGA_CORREIOS, R.drawable.ic_correios_white)
        }
        btn_porta.setOnClickListener {
            toggleSelectedOption(it, FormaEntrega.FORMA_ENTREGA_PORTA, R.drawable.ic_porta_white)
        }
        btn_portao.setOnClickListener {
            toggleSelectedOption(it, FormaEntrega.FORMA_ENTREGA_PORTAO, R.drawable.ic_portao_white)
        }
        btn_vizinho_esquerdo.setOnClickListener {
            toggleSelectedOption(it, FormaEntrega.FORMA_ENTREGA_VIZINHO_ESQUERDA, R.drawable.ic_esquerda_white)
        }
        btn_vizinho_direito.setOnClickListener {
            toggleSelectedOption(it, FormaEntrega.FORMA_ENTREGA_VIZINHO_DIREITA, R.drawable.ic_direita_white)
        }


        val list = listOf("NAO LOCALIZADO", "IMOVEL DEMOLIDO", "IMOVEL ABANDONADO",
                "IMOVEL EM OBRAS", "LOCAL VAZIO", "NOME INCORRETO", "DUPLICIDADE", "LOCAL PERIGOSO",
                "ERRO DE LEITURA", "OUTRO CONTEXTO").toTypedArray()
        btn_not_delivered.setOnClickListener {
            selectedForma = FormaEntrega.FORMA_ENTREGA_NAO_ENTREGUE
            AlertDialog.Builder(context)
                    .setTitle("Selecione o tipo de Ocorrência")
                    .setItems(list) { _, position ->
                        tvMotivo.text = list[position]
                    }.show()
        }

        btn_save.setOnClickListener {
            selectedForma?.let {
                saveListener?.invoke(it)
            }
        }
    }

    private fun toggleSelectedOption(view: View, formaEntrega: FormaEntrega, drawableRes: Int) {
        selectedForma = formaEntrega
        btn_maos.isSelected = false
        btn_correios.isSelected = false
        btn_porta.isSelected = false
        btn_portao.isSelected = false
        btn_vizinho_esquerdo.isSelected = false
        btn_vizinho_direito.isSelected = false
        view.isSelected = true
        resetDrawables()
        updateDrawable(view as Button, drawableRes)
    }

    private fun resetDrawables() {
        updateDrawable(btn_maos, R.drawable.ic_mao)
        updateDrawable(btn_correios, R.drawable.ic_correios)
        updateDrawable(btn_porta, R.drawable.ic_porta)
        updateDrawable(btn_portao, R.drawable.ic_portao)
        updateDrawable(btn_vizinho_esquerdo, R.drawable.ic_esquerda)
        updateDrawable(btn_vizinho_direito, R.drawable.ic_direita)
    }

    private fun updateDrawable(button: Button, drawableRes: Int) {
        button.setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(context, drawableRes), null, null)
    }

    fun setSaveListener(saveListener: ((formaEntrega: FormaEntrega) -> Unit)?): FormaEntregaDialog {
        this.saveListener = saveListener
        return this
    }
}