package wacode.yamada.yuki.nempaymentapp.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import wacode.yamada.yuki.nempaymentapp.R

class SimpleMessageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_simple_message, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        view?.findViewById<TextView>(R.id.messageTextView)?.text = getMessage()
    }

    private fun getMessage() = arguments.getString(ARG_MESSAGE, "")

    companion object {
        private const val ARG_MESSAGE = "message"
        fun newInstance(message: String): SimpleMessageFragment {
            val fragment = SimpleMessageFragment()
            val args = Bundle().apply {
                putString(ARG_MESSAGE, message)
            }
            fragment.arguments = args
            return fragment
        }
    }
}