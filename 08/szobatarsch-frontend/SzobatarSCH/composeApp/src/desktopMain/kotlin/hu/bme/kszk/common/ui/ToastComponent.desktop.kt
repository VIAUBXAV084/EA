package hu.bme.kszk.common.ui

// Desktop natív Snackbar
import androidx.compose.runtime.Composable
import javax.swing.JOptionPane

@Composable
actual fun ShowToast(message: String) {
    JOptionPane.showMessageDialog(null, message, "Üzenet", JOptionPane.INFORMATION_MESSAGE)
}