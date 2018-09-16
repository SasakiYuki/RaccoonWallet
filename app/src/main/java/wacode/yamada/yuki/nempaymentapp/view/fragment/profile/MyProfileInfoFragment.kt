package wacode.yamada.yuki.nempaymentapp.view.fragment.profile

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.isseiaoki.simplecropview.CropImageView
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_my_profile_info.*
import wacode.yamada.yuki.nempaymentapp.R
import wacode.yamada.yuki.nempaymentapp.di.ViewModelFactory
import wacode.yamada.yuki.nempaymentapp.extentions.getColorFromResource
import wacode.yamada.yuki.nempaymentapp.extentions.getDrawable
import wacode.yamada.yuki.nempaymentapp.room.profile.MyProfile
import wacode.yamada.yuki.nempaymentapp.view.activity.CropImageActivity
import wacode.yamada.yuki.nempaymentapp.view.fragment.BaseFragment
import wacode.yamada.yuki.nempaymentapp.viewmodel.MyProfileInfoViewModel
import javax.inject.Inject

class MyProfileInfoFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var myProfileInfoViewModel: MyProfileInfoViewModel
    override fun layoutRes() = R.layout.fragment_my_profile_info

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        myProfileInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyProfileInfoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupViewModel()
    }

    private fun setupViews() {
        myProfileInfoViewModel.onInit()
        disableEditTexts()
        disableEditImageViews()
    }

    private fun setupImageViewsClickListener() {
        circleImageView.setOnClickListener {
            startActivityForResult(CropImageActivity.createIntent(circleImageView.context, CropImageView.CropMode.CIRCLE_SQUARE), REQUEST_CODE_USER_ICON)
        }
        userScreenImageView.setOnClickListener {
            startActivityForResult(CropImageActivity.createIntent(userScreenImageView.context, CropImageView.CropMode.RATIO_16_9), REQUEST_CODE_USER_SCREEN_IMAGE)
        }
    }

    private fun enableEditImageViews() {
        changeImageIconImageView.visibility = View.VISIBLE
        changeImageIconTextView.visibility = View.VISIBLE
        changeUserIconImageView.visibility = View.VISIBLE
        changeUserIconTextView.visibility = View.VISIBLE
        setupImageViewsClickListener()
        circleImageView.background = getDrawable(circleImageView.context, R.drawable.foreground_circle_icon_gray_scale)
    }

    private fun disableEditImageViews() {
        changeImageIconImageView.visibility = View.GONE
        changeImageIconTextView.visibility = View.GONE
        changeUserIconImageView.visibility = View.GONE
        changeUserIconTextView.visibility = View.GONE
        resetImageViewsClickListener()
        circleImageView.setBackgroundColor(circleImageView.context.getColorFromResource(android.R.color.transparent))
    }

    private fun resetImageViewsClickListener() {
        circleImageView.setOnClickListener {}
        userScreenImageView.setOnClickListener {}
    }

    private fun enableEditTexts() {
        nameEditText.isEnabled = true
        rubyEdiText.isEnabled = true
        phoneNumberEditText.isEnabled = true
        mailAddressEditText.isEnabled = true
    }

    private fun disableEditTexts() {
        disableEditText(nameEditText)
        disableEditText(rubyEdiText)
        disableEditText(phoneNumberEditText)
        disableEditText(mailAddressEditText)
    }

    private fun disableEditText(editText: EditText) {
        editText.apply {
            isEnabled = false
            setTextColor(context.getColorFromResource(R.color.textBlack))
        }
    }

    private fun setupViewModel() {
        myProfileInfoViewModel.apply {
            myAddressCountLiveData
                    .observe(this@MyProfileInfoFragment, Observer {
                        it ?: return@Observer
                        walletCountTextView.text = it.toString()
                    })
            bottomEditButtonEventLiveData
                    .observe(this@MyProfileInfoFragment, Observer {
                        it ?: return@Observer
                        enableEditTexts()
                        enableEditImageViews()
                    })
            bottomCompleteButtonEventLiveData
                    .observe(this@MyProfileInfoFragment, Observer {
                        it ?: return@Observer
                        createMyProfile()
                        disableEditTexts()
                        disableEditImageViews()
                    })
            myProfileLiveData
                    .observe(this@MyProfileInfoFragment, Observer {
                        it ?: return@Observer
                        setMyProfileInformation(it)
                    })
            updateEventLiveData
                    .observe(this@MyProfileInfoFragment, Observer {
                        it ?: return@Observer
                        // do nothing
                    })
        }
    }

    private fun setMyProfileInformation(myProfile: MyProfile?) {
        myProfile?.let {
            nameEditText.setText(it.name)
            rubyEdiText.setText(it.nameRuby)
            phoneNumberEditText.setText(it.phoneNumber)
            mailAddressEditText.setText(it.mailAddress)
        }
    }

    private fun createMyProfile() {
        myProfileInfoViewModel.create(MyProfile(
                0,
                nameEditText.text.toString(),
                rubyEdiText.text.toString(),
                phoneNumberEditText.text.toString(),
                mailAddressEditText.text.toString(),
                false
        ))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_USER_ICON -> {
                    setCircleImage(data)
                }
                REQUEST_CODE_USER_SCREEN_IMAGE -> {
                    setUserScreenImageView(data)
                }
            }
        }
    }

    private fun setCircleImage(intent: Intent?) {
        intent?.let {
            val uriString = it.getStringExtra(CropImageActivity.PARAM_INTENT_RESULT_URI)
            Picasso.with(circleImageView.context).load(uriString).into(circleImageView)
            circleImageView.tag = uriString
        }
    }

    private fun setUserScreenImageView(intent: Intent?) {
        intent?.let {
            val uriString = it.getStringExtra(CropImageActivity.PARAM_INTENT_RESULT_URI)
            Picasso.with(userScreenImageView.context).load(uriString).into(userScreenImageView)
            userScreenImageView.tag = uriString
        }
    }

    companion object {
        private const val REQUEST_CODE_USER_ICON = 1129
        private const val REQUEST_CODE_USER_SCREEN_IMAGE = 710
        fun newInstance(): MyProfileInfoFragment {
            return MyProfileInfoFragment().apply {
                val args = Bundle()
                args.putInt(ARG_CONTENTS_NAME_ID, R.string.my_address_profile_activity_tab_profile)
                arguments = args
            }
        }
    }
}
