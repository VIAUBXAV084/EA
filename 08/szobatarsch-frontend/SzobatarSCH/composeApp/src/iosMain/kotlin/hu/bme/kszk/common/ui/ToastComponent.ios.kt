package hu.bme.kszk.common.ui

// iOS natív UIAlertController
import androidx.compose.runtime.Composable
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

@Composable
actual fun ShowToast(message: String) {
    val alert = UIAlertController.alertControllerWithTitle(
        title = null,
        message = message,
        preferredStyle = UIAlertControllerStyleAlert
    )
    alert.addAction(UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault, null))

    val controller = UIApplication.sharedApplication.keyWindow?.rootViewController
    controller?.presentViewController(alert, animated = true, completion = null)
}