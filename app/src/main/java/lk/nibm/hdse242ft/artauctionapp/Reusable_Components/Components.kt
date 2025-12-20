package lk.nibm.hdse242ft.artauctionapp.Reusable_Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lk.nibm.hdse242ft.artauctionapp.ui.theme.ButtonBg
import lk.nibm.hdse242ft.artauctionapp.ui.theme.ButtonText
import lk.nibm.hdse242ft.artauctionapp.ui.theme.HeadingColor
import lk.nibm.hdse242ft.artauctionapp.ui.theme.SecondaryBg
import lk.nibm.hdse242ft.artauctionapp.ui.theme.TextColor
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Checkbox
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.core.content.FileProvider
import lk.nibm.hdse242ft.artauctionapp.ui.theme.ButtonHoverBg
import java.io.File
import android.content.Context
import android.util.Base64
import java.io.InputStream
import androidx.compose.material3.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import lk.nibm.hdse242ft.artauctionapp.ui.theme.BgColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import lk.nibm.hdse242ft.artauctionapp.ui.theme.ButtonBg

// Login Sign up reusable component
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null, // ✅ add this
    isPassword: Boolean = false,
    labelColor: Color = ButtonBg,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = labelColor) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon ?: run {
            if (isPassword) {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val desc = if (passwordVisible) "Hide password" else "Show password"
                {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = desc, tint = HeadingColor)
                    }
                }
            } else null
        },
        singleLine = true,
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SecondaryBg,
            unfocusedContainerColor = SecondaryBg,
            focusedTextColor = TextColor,
            unfocusedTextColor = TextColor,
            cursorColor = HeadingColor,
            focusedLabelColor = labelColor,
            unfocusedLabelColor = labelColor,
            focusedLeadingIconColor = HeadingColor,
            unfocusedLeadingIconColor = TextColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}


@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SecondaryBg,
            contentColor = TextColor
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally, // ✅ Center all children
            content = content
        )
    }
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: Int = 18
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonBg,
            contentColor = ButtonText
        ),
        shape = RoundedCornerShape(26.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(text = text, fontSize = fontSize.sp)
    }
}
@Composable
fun AppClickableText(
    text: String,
    color: Color,
    fontSize: Int = 14,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth()        // ✅ Only as wide as the text
            .clickable { onClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleDropdown(
    selectedRole: String,
    roles: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onRoleSelected: (String) -> Unit
) {
    val hoverBg = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) // item bg
    val textColor = ButtonHoverBg // #FF6D4D

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedRole,
            onValueChange = {},
            readOnly = true,
            label = { Text("Role", color = ButtonBg) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SecondaryBg,
                unfocusedContainerColor = SecondaryBg,
                focusedTextColor = TextColor,
                unfocusedTextColor = TextColor,
                cursorColor = HeadingColor,
                focusedLabelColor = ButtonBg,
                unfocusedLabelColor = ButtonBg,
                focusedLeadingIconColor = HeadingColor,
                unfocusedLeadingIconColor = TextColor
            ),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            roles.forEach { role ->
                DropdownMenuItem(
                    text = { Text(role, color = textColor) },
                    colors = MenuDefaults.itemColors(
                        textColor = textColor,
                        leadingIconColor = textColor,
                        trailingIconColor = textColor,
                        disabledTextColor = textColor.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.background(hoverBg), // ✅ background via modifier
                    onClick = {
                        onRoleSelected(role)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}



@Composable
fun CheckboxWithClickableText(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    clickableSpans: List<Triple<String, Color, () -> Unit>>, // word, color, action
    defaultColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )

        val annotatedString = buildAnnotatedString {
            var currentIndex = 0

            clickableSpans.forEach { (spanText, spanColor, _) ->
                val start = text.indexOf(spanText, currentIndex)
                if (start >= 0) {
                    // Add normal text before clickable span
                    if (start > currentIndex) {
                        append(text.substring(currentIndex, start))
                    }

                    // Add clickable span
                    withStyle(style = SpanStyle(color = spanColor)) {
                        pushStringAnnotation(tag = spanText, annotation = spanText)
                        append(spanText)
                        pop()
                    }
                    currentIndex = start + spanText.length
                }
            }

            // Add remaining normal text
            if (currentIndex < text.length) {
                append(text.substring(currentIndex, text.length))
            }
        }

        ClickableText(
            text = annotatedString,
            style = androidx.compose.ui.text.TextStyle(color = defaultColor, fontSize = 14.sp),
            onClick = { offset ->
                clickableSpans.forEach { (spanText, _, action) ->
                    annotatedString.getStringAnnotations(tag = spanText, start = offset, end = offset)
                        .firstOrNull()?.let { action() }
                }
            }
        )
    }
}

/** TextField for Forgot Password / OTP screens */
//Forget Password screen text feild
@Composable
fun FPTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        enabled = enabled,
        leadingIcon = leadingIcon,
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors()
    )
}
@Composable
fun FPPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = ButtonBg) },
        singleLine = true,
        enabled = enabled,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Default.Visibility
            else Icons.Default.VisibilityOff

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password" , tint = HeadingColor)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SecondaryBg,
            unfocusedContainerColor = SecondaryBg,
            focusedTextColor = TextColor,
            unfocusedTextColor = TextColor,
            cursorColor = HeadingColor,
            focusedLabelColor = ButtonBg,
            unfocusedLabelColor = ButtonBg,
            focusedLeadingIconColor = HeadingColor,
            unfocusedLeadingIconColor = TextColor
        ),
        modifier = modifier.fillMaxWidth()
    )
}


/** Card container */
@Composable
fun FPCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = SecondaryBg, contentColor = TextColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            content = content
        )
    }
}

/** Button container */
@Composable
fun FPButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonBg, contentColor = ButtonText),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp)
    }
}





// List New Art work reusable component
// --- Text Field ---
@Composable
fun AuctionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}


// --- Dropdown ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuctionDropdown(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange, modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

// --- Card Section ---
@Composable
fun SectionCard(title: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}

// --- Image Picker ---

@Composable
fun ImagePickerWithDialog(
    selectedImageUri: Uri?,
    onImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    // State to hold camera URI
    val cameraUriState = remember { mutableStateOf<Uri?>(null) }

    // Launchers
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && cameraUriState.value != null) {
            onImageSelected(cameraUriState.value!!)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraUriState.value?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // UI Box
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Gray.copy(alpha = 0.2f))
            .clickable { showDialog = true },
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = "Selected Artwork Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Image, contentDescription = "Upload Image", modifier = Modifier.size(48.dp))
                Spacer(Modifier.height(8.dp))
                Text("Tap to Upload Image")
            }
        }
    }

    // Dialog for selecting Camera or Gallery
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Image") },
            text = { Text("Choose Camera or Gallery") },
            confirmButton = {
                TextButton(onClick = {
                    val imagesDir = File(context.cacheDir, "profile_images")
                    if (!imagesDir.exists()) imagesDir.mkdirs()

                    val file = File(imagesDir, "profile_${System.currentTimeMillis()}.jpg")
                    val cameraUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                    cameraUriState.value = cameraUri

                    // Request permission before launching camera
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    showDialog = false
                }) { Text("Camera") }
            },
            dismissButton = {
                TextButton(onClick = {
                    galleryLauncher.launch("image/*")
                    showDialog = false
                }) { Text("Gallery") }
            }
        )
    }
}

// --- Convert Uri to Bitmap Upload Image---
fun uriToBase64_List_New_artwork(context: Context, uri: Uri): String {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val bytes = inputStream?.readBytes() ?: ByteArray(0)
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}
@Composable
fun DateTimePickerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier // 👈 allow custom modifier
) {
    val context = LocalContext.current

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = modifier, // 👈 now uses passed modifier
        trailingIcon = {
            IconButton(onClick = {
                // Step 1: Pick a Date
                val calendar = java.util.Calendar.getInstance()
                val datePicker = android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        // Step 2: Pick a Time after Date
                        val timePicker = android.app.TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                val formatted = String.format(
                                    "%04d-%02d-%02d %02d:%02d",
                                    year, month + 1, dayOfMonth, hour, minute
                                )
                                onValueChange(formatted)
                            },
                            calendar.get(java.util.Calendar.HOUR_OF_DAY),
                            calendar.get(java.util.Calendar.MINUTE),
                            true
                        )
                        timePicker.show()
                    },
                    calendar.get(java.util.Calendar.YEAR),
                    calendar.get(java.util.Calendar.MONTH),
                    calendar.get(java.util.Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Pick Date")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    userName: String = "Guest",
    userPhoto: String? = null, // Pass full URL now
    profileIcon: ImageVector = Icons.Default.Person,
    onProfileClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
//                        .background(HeadingColor)
                        .background(if (!userPhoto.isNullOrEmpty()) Color.Transparent else HeadingColor)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (!userPhoto.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(userPhoto),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop, // <-- make image fill the box
                            modifier = Modifier
                                .size(40.dp) // same as Box size
                                .clip(CircleShape) // make image circular
                        )
                    } else {
                        Icon(
                            imageVector = profileIcon,
                            contentDescription = "Profile Picture",
                            tint = ButtonText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }


                Column {
                    Text(
                        userName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ButtonBg
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onNotificationClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = HeadingColor
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = SecondaryBg)
    )
}






@Composable
fun AppBottomBar(
    navController: NavController,
    currentScreen: String,
    items: List<String> = listOf("Dashboard", "My Art", "Profile")
) {
    NavigationBar(containerColor = SecondaryBg) {
        items.forEach { item ->
            val isSelected = item == currentScreen
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    when (item) {
                        "Dashboard" -> navController.navigate("artist_dashboard_screen") { launchSingleTop = true; restoreState = true }
                        "My Art" -> navController.navigate("my_art_screen") { launchSingleTop = true; restoreState = true }
                        "Profile" -> navController.navigate("profile_screen") { launchSingleTop = true; restoreState = true }
                    }
                },
                icon = {
                    val icon = when (item) {
                        "Dashboard" -> Icons.Default.Home
                        "My Art" -> Icons.Default.Palette
                        "Profile" -> Icons.Default.Person
                        else -> Icons.Default.Home
                    }
                    Icon(icon, contentDescription = item, tint = if (isSelected) HeadingColor else TextColor)
                },
                label = { Text(item, color = if (isSelected) HeadingColor else TextColor, maxLines = 1) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = BgColor)
            )
        }
    }
}

