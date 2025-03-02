package com.anvipus.explore.ui.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.navigation.fragment.navArgs
import com.anvipus.core.models.UserDetail
import com.anvipus.core.utils.state.AccountManager
import com.anvipus.explore.base.BaseComposeFragment
import com.anvipus.explore.databinding.UserDetailFragmentBinding
import com.codedisruptors.dabestofme.di.Injectable
import coil.compose.AsyncImage
import javax.inject.Inject

class UserDetailComposeFragment : BaseComposeFragment(), Injectable {
    companion object {
        fun newInstance() = UserDetailComposeFragment()
        private val TAG = "UserDetailComposeFragment"
    }

    override val showToolbar: Boolean get() = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mBinding: UserDetailFragmentBinding
    private val params by navArgs<UserDetailComposeFragmentArgs>()

    @Inject
    lateinit var accountManager: AccountManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        defaultLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                ownTitle(params.data.login!!)
            }
        }
        lifecycle.addObserver(defaultLifecycleObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val userDetail = params.data
                userDetail?.let {
                    UserDetailScreen(it)
                } ?: run {
                    Text("No user data available", modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    @Composable
    fun UserDetailScreen(userDetail: UserDetail) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val (profileImage, usernameText, fullNameText, emailText, biographyLabel, biographyValue) = createRefs()

                AsyncImage(
                    model = userDetail.avatar_url,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .constrainAs(profileImage) {
                            top.linkTo(parent.top, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .size(150.dp)
                        .background(Color.Gray, shape = CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = null
                )

                // Username
                Text(
                    text = userDetail.username ?: "Username",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E2E2E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .constrainAs(usernameText) {
                            top.linkTo(profileImage.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                // Fullname
                Text(
                    text = userDetail.fullname ?: "Fullname",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E2E2E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .constrainAs(fullNameText) {
                            top.linkTo(usernameText.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                // Email
                Text(
                    text = userDetail.emailFull ?: "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E2E2E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .constrainAs(emailText) {
                            top.linkTo(fullNameText.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                // Biography Label
                Text(
                    text = "Url Github",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E2E2E),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .constrainAs(biographyLabel) {
                            top.linkTo(emailText.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                val url = userDetail.html_url ?: "URL"
                val annotatedString = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(url)
                        // Menandai bagian ini sebagai tautan
                        addStringAnnotation(
                            tag = "URL",
                            annotation = url,
                            start = 0,
                            end = url.length
                        )
                    }
                }

                ClickableText(
                    text = annotatedString,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Start
                    ),
                    modifier = Modifier
                        .constrainAs(biographyValue) {
                            top.linkTo(biographyLabel.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                            .firstOrNull()?.let { annotation ->
                                // Membuka tautan di browser
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                requireContext().startActivity(intent)
                            }
                    }
                )
            }
        }
    }
}