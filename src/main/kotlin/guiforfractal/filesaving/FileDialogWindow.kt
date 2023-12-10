package guiforfractal.filesaving


import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import javax.swing.filechooser.FileNameExtensionFilter


fun fileDialogWindow(photo: BufferedImage) {
    val fileDialogFrame = JFrame()
    fileDialogFrame.defaultCloseOperation = EXIT_ON_CLOSE
    val fileChooser = JFileChooser()

    val filter = FileNameExtensionFilter(
        "Фотографии", "jpg"
    )

    fileChooser.fileFilter = filter
    fileChooser.dialogTitle = "Сохранение фрактала"
    fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY;


    var result = fileChooser.showSaveDialog(fileDialogFrame)


    if (File("${fileChooser.selectedFile}.jpg").exists()) {
        JOptionPane.showMessageDialog(
            fileDialogFrame,
            "Файл '" + fileChooser.selectedFile +
                    " существует"
        )
        fileChooser.showSaveDialog(fileDialogFrame)
    }
    else{
        try {
            if (fileChooser.selectedFile != null) {
                ImageIO.write(photo, "jpg", File("${fileChooser.selectedFile}.jpg"))
                JOptionPane.showMessageDialog(
                    fileDialogFrame,
                    "Файл '" + fileChooser.selectedFile +
                            " сохранен"
                );
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}