package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.fragment_first_tutorial.*
import wacode.yamada.yuki.nempaymentapp.R

class FirstTutorialFragment : BaseFragment() {
    private val position by lazy {
        arguments.getInt(ARGS_POSITION)
    }

    override fun layoutRes() = R.layout.fragment_first_tutorial

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView.setImageDrawable(ContextCompat.getDrawable(context, setTutorialImageView()))
    }

    private fun setTutorialImageView(): Int {
        return when (position) {
            0 -> R.mipmap.image_onboard1
            1 -> R.mipmap.image_onboard2
            2 -> R.mipmap.image_onboard3
            else -> {
                R.mipmap.image_onboard1
            }
        }
    }

    companion object {
        private const val ARGS_POSITION = "args_position"
        fun newInsntace(position: Int): FirstTutorialFragment {
            val fragment = FirstTutorialFragment()
            val args = Bundle()
            args.putInt(ARGS_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }
}