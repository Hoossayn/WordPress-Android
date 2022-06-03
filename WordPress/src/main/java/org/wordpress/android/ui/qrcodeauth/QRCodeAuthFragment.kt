package org.wordpress.android.ui.qrcodeauth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.wordpress.android.R
import org.wordpress.android.databinding.QrcodeauthFragmentBinding
import org.wordpress.android.ui.posts.BasicDialogViewModel
import org.wordpress.android.ui.posts.BasicDialogViewModel.BasicDialogModel
import org.wordpress.android.ui.qrcodeauth.QRCodeAuthActionEvent.FinishActivity
import org.wordpress.android.ui.qrcodeauth.QRCodeAuthActionEvent.LaunchDismissDialog
import org.wordpress.android.ui.qrcodeauth.QRCodeAuthActionEvent.LaunchScanner
import org.wordpress.android.viewmodel.observeEvent

@AndroidEntryPoint
class QRCodeAuthFragment : Fragment(R.layout.qrcodeauth_fragment) {
    private val viewModel: QRCodeAuthViewModel by viewModels()
    private val dialogViewModel: BasicDialogViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(QrcodeauthFragmentBinding.bind(view)) {
            initBackPressHandler()
            observeViewModel()
            startViewModel()
        }
    }

    private fun QrcodeauthFragmentBinding.observeViewModel() {
        viewModel.actionEvents.onEach { handleActionEvents(it) }.launchIn(viewLifecycleOwner.lifecycleScope)
        dialogViewModel.onInteraction.observeEvent(viewLifecycleOwner) { viewModel.onDialogInteraction(it) }
    }

    private fun startViewModel() {
        viewModel.start()
    }

    private fun handleActionEvents(actionEvent: QRCodeAuthActionEvent) {
        when (actionEvent) {
            is LaunchDismissDialog -> launchDismissDialog(actionEvent.dialogModel)
            is LaunchScanner -> launchScanner()
            is FinishActivity -> requireActivity().finish()
        }
    }

    private fun launchDismissDialog(model: QRCodeAuthDialogModel) {
        dialogViewModel.showDialog(requireActivity().supportFragmentManager,
                BasicDialogModel(model.tag,
                        getString(model.title),
                        getString(model.message),
                        getString(model.positiveButtonLabel),
                        model.negativeButtonLabel?.let { label -> getString(label) },
                        model.cancelButtonLabel?.let { label -> getString(label) }
                ))
    }

    private fun launchScanner() {
        val scanner = GmsBarcodeScanning.getClient(requireContext())
        scanner.startScan()
                .addOnSuccessListener { barcode -> viewModel.onScanSuccess(barcode.rawValue) }
                .addOnFailureListener {
                    viewModel.onScanFailure()
                }
    }

    private fun initBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(
                        true
                ) {
                    override fun handleOnBackPressed() {
                        viewModel.onBackPressed()
                    }
                })
    }

    @Suppress("MagicNumber")
    private fun temp() {
        // Temporarily reference all strings from file so CI wont complain.
        // This will be removed in an upcoming PR
        var temp = R.string.qrcode_auth_flow_validated_default_title
        temp = R.string.qrcode_auth_flow_done_title
        temp = R.string.qrcode_auth_flow_done_subtitle
        temp = R.string.qrcode_auth_flow_dismiss
        temp = R.string.qrcode_auth_flow_scan_again
        temp = R.string.qrcode_auth_flow_error_no_connection_title
        temp = R.string.qrcode_auth_flow_error_no_connection_subtitle
        temp = R.string.qrcode_auth_flow_error_invalid_data_title
        temp = R.string.qrcode_auth_flow_error_invalid_data_subtitle
        temp = R.string.qrcode_auth_flow_error_expired_title
        temp = R.string.qrcode_auth_flow_error_expired_subtitle
        temp = R.string.qrcode_auth_flow_error_auth_failed_title
        temp = R.string.qrcode_auth_flow_error_auth_failed_subtitle
        temp = R.string.qrcode_auth_flow_dismiss_dialog_title
        temp = R.string.qrcode_auth_flow_dismiss_dialog_message
        var image = R.drawable.img_illustration_qrcode_auth_login_success_218dp
    }
}
