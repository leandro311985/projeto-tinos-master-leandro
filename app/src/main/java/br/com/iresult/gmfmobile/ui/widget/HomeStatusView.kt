package br.com.iresult.gmfmobile.ui.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import br.com.iresult.gmfmobile.R

enum class HomeStatus {
    CONCLUIDO,
    NAO_ENTREGUE,
    AGORA,
    PROXIMO,
    NONE
}

class HomeStatusView(context: Context, attr: AttributeSet) : ConstraintLayout(context, attr) {

    private val parendId = 1
    val tvNumberHome = TextView(context)
    private val tvTitleNowNext = TextView(context)

    fun setupLayout(status: HomeStatus) {
        this.id = parendId
        this.setBackgroundResource(R.drawable.unidade_bg)
        this.setBackgroundResource(status.getBackgroundContainer())

        tvNumberHome.also { tv ->
            tv.textSize = 24f
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.setTextColor(status.textColor())
            tv.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            tv.layoutParams = LayoutParams(LayoutParams.MATCH_CONSTRAINT, LayoutParams.WRAP_CONTENT).also { lp ->
                lp.endToEnd = this.id
                lp.bottomToBottom = this.id
                lp.leftToLeft = this.id
                lp.marginEnd = 15.toPx()
                lp.bottomMargin = 15.toPx()
            }
        }

        (tvNumberHome.parent as? ViewGroup)?.removeView(tvNumberHome)
        this.addView(tvNumberHome)


        tvTitleNowNext.also { tv ->
            tv.textSize = 12f
            tv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDarker))
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.text = status.getTextStatus()
            tv.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT).also { lp ->
                lp.startToStart = this.id
                lp.topToTop = this.id
                lp.marginStart = 13.toPx()
                lp.topMargin = 13.toPx()
            }
        }
        (tvTitleNowNext.parent as? ViewGroup)?.removeView(tvTitleNowNext)
        this.addView(tvTitleNowNext)
        this.addView(ImageView(context).also { iv ->
            iv.setImageResource(R.drawable.ic_unidade_checked)
            iv.visibility = status.getVisibility()
            iv.layoutParams = LayoutParams(20.toPx(), 20.toPx()).also { lp ->
                lp.startToStart = this.id
                lp.topToTop = this.id
                lp.marginStart = 13.toPx()
                lp.topMargin = 13.toPx()
            }
        })
    }

    private fun HomeStatus.getBackgroundContainer() = when(this) {
        HomeStatus.CONCLUIDO -> R.drawable.unidade_bg_completed
        HomeStatus.NAO_ENTREGUE -> R.drawable.unidade_bg_not_delivered
        HomeStatus.AGORA -> R.drawable.unidade_bg_current
        HomeStatus.PROXIMO -> R.drawable.unidade_bg_next
        HomeStatus.NONE -> R.drawable.unidade_bg
    }

    private fun HomeStatus.getVisibility() = when(this) {
        HomeStatus.CONCLUIDO, HomeStatus.NAO_ENTREGUE -> View.VISIBLE
        else -> View.GONE
    }

    private fun HomeStatus.getTextStatus() = when(this) {
        HomeStatus.AGORA -> "Agora"
        HomeStatus.PROXIMO -> "Próxima"
        else -> ""
    }

    private fun HomeStatus.textColor() = ContextCompat.getColor(context, when(this) {
        HomeStatus.CONCLUIDO -> R.color.whiteOpaqueDividerColor
        HomeStatus.NAO_ENTREGUE, HomeStatus.NONE -> R.color.colorPrimaryDarker
        HomeStatus.AGORA -> R.color.white
        HomeStatus.PROXIMO -> R.color.colorAccent
    })

    private fun Int.toPx() = (this * context.resources.displayMetrics.density).toInt()
}