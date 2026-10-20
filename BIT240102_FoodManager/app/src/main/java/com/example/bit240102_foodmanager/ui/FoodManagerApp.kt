package com.example.bit240102_foodmanager.ui

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.bit240102_foodmanager.R
import com.example.bit240102_foodmanager.data.AppLanguage
import com.example.bit240102_foodmanager.data.DishCategory
import com.example.bit240102_foodmanager.data.DishImage
import com.example.bit240102_foodmanager.data.DishSort
import com.example.bit240102_foodmanager.data.DishWithDetails
import com.example.bit240102_foodmanager.services.MockApiServiceFactory
import com.example.bit240102_foodmanager.services.SyncScheduler
import java.util.Locale

private val BIT240102_Green = androidx.compose.ui.graphics.Color(0xFF21835A)
private val BIT240102_Background = androidx.compose.ui.graphics.Color(0xFFF4F6F1)
private val BIT240102_Orange = androidx.compose.ui.graphics.Color(0xFFC97834)

private val BIT240102_StringResources = mapOf(
    "app_name" to R.string.app_name,
    "home_title" to R.string.home_title,
    "categories_title" to R.string.categories_title,
    "add_dish" to R.string.add_dish,
    "add_category" to R.string.add_category,
    "edit_dish" to R.string.edit_dish,
    "edit_category" to R.string.edit_category,
    "detail_title" to R.string.detail_title,
    "switch_language" to R.string.switch_language,
    "search_hint" to R.string.search_hint,
    "category_filter" to R.string.category_filter,
    "all_categories" to R.string.all_categories,
    "availability_filter" to R.string.availability_filter,
    "all_statuses" to R.string.all_statuses,
    "available_only" to R.string.available_only,
    "unavailable_only" to R.string.unavailable_only,
    "sort_by" to R.string.sort_by,
    "sort_name" to R.string.sort_name,
    "sort_price_asc" to R.string.sort_price_asc,
    "sort_price_desc" to R.string.sort_price_desc,
    "sort_time_asc" to R.string.sort_time_asc,
    "min_price" to R.string.min_price,
    "max_price" to R.string.max_price,
    "no_dishes" to R.string.no_dishes,
    "no_categories" to R.string.no_categories,
    "main_courses" to R.string.main_courses,
    "appetizers" to R.string.appetizers,
    "desserts" to R.string.desserts,
    "available" to R.string.available,
    "unavailable" to R.string.unavailable,
    "quick" to R.string.quick,
    "regular" to R.string.regular,
    "price_format" to R.string.price_format,
    "name_label" to R.string.name_label,
    "price_label" to R.string.price_label,
    "time_label" to R.string.time_label,
    "category_label" to R.string.category_label,
    "select_category" to R.string.select_category,
    "description_label" to R.string.description_label,
    "images_label" to R.string.images_label,
    "images_help" to R.string.images_help,
    "available_toggle" to R.string.available_toggle,
    "save" to R.string.save,
    "cancel" to R.string.cancel,
    "edit" to R.string.edit,
    "delete" to R.string.delete,
    "delete_dish_title" to R.string.delete_dish_title,
    "delete_dish_message" to R.string.delete_dish_message,
    "delete_category_title" to R.string.delete_category_title,
    "delete_category_message" to R.string.delete_category_message,
    "delete_confirm" to R.string.delete_confirm,
    "thumbnail" to R.string.thumbnail,
    "set_thumbnail" to R.string.set_thumbnail,
    "no_images" to R.string.no_images,
    "quick_label" to R.string.quick_label,
    "regular_label" to R.string.regular_label,
    "price_detail" to R.string.price_detail,
    "preparation_detail" to R.string.preparation_detail,
    "category_description_label" to R.string.category_description_label,
    "offline_notice" to R.string.offline_notice,
    "configure_api_notice" to R.string.configure_api_notice,
    "category_in_use" to R.string.category_in_use,
    "dish_not_found" to R.string.dish_not_found,
    "error_name_required" to R.string.error_name_required,
    "error_price_invalid" to R.string.error_price_invalid,
    "error_time_invalid" to R.string.error_time_invalid,
    "error_category_required" to R.string.error_category_required,
    "error_duplicate_name" to R.string.error_duplicate_name,
    "error_image_url" to R.string.error_image_url,
    "error_image_not_found" to R.string.error_image_not_found,
    "error_category_name" to R.string.error_category_name,
    "error_price_filter" to R.string.error_price_filter,
    "error_price_range" to R.string.error_price_range,
    "error_not_found" to R.string.error_not_found,
    "error_unexpected" to R.string.error_unexpected,
    "validation_error" to R.string.validation_error,
    "back" to R.string.back,
)

private class AppStrings(context: Context) {
    private val resources = context.resources

    fun get(key: String): String = resources.getString(resourceId(key))

    fun format(key: String, vararg arguments: Any): String =
        resources.getString(resourceId(key), *arguments)

    fun minutes(quantity: Int): String =
        resources.getQuantityString(R.plurals.minutes, quantity, quantity)

    private fun resourceId(key: String): Int =
        BIT240102_StringResources[key] ?: error("Missing localized string resource: $key")
}

@Composable
private fun rememberStrings(language: AppLanguage): AppStrings {
    val context = LocalContext.current
    val baseConfiguration = LocalConfiguration.current
    return remember(context, language, baseConfiguration) {
        val configuration = Configuration(baseConfiguration).apply {
            setLocale(Locale.forLanguageTag(language.BIT240102_Tag))
        }
        AppStrings(context.createConfigurationContext(configuration))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodManagerApp(viewModel: FoodManagerViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val destination = backStack?.destination?.route ?: "home"
    val strings = rememberStrings(uiState.BIT240102_Language)
    val context = LocalContext.current
    var online by remember { mutableStateOf(isConnected(context)) }
    val configured = remember(context) { MockApiServiceFactory.isConfigured(context) }

    DisposableEffect(context) {
        val manager = context.getSystemService(ConnectivityManager::class.java)
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                online = true
                SyncScheduler.schedule(context)
            }

            override fun onLost(network: Network) {
                online = isConnected(context)
            }
        }
        try {
            manager.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build(),
                callback,
            )
        } catch (_: SecurityException) {
            online = false
        }
        onDispose {
            try {
                manager.unregisterNetworkCallback(callback)
            } catch (_: IllegalArgumentException) {
                online = false
            }
        }
    }

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = BIT240102_Green,
            secondary = BIT240102_Orange,
            background = BIT240102_Background,
            surface = androidx.compose.ui.graphics.Color.White,
        ),
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.statusBars,
            containerColor = BIT240102_Background,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            when {
                                destination == "categories" -> strings.get("categories_title")
                                destination == "home" -> strings.get("home_title")
                                destination.startsWith("detail") -> strings.get("detail_title")
                                destination == "form/new" -> strings.get("add_dish")
                                destination.startsWith("form/") -> strings.get("edit_dish")
                                else -> strings.get("home_title")
                            },
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    navigationIcon = {
                        if (destination != "home") {
                            TextButton(onClick = { navController.navigateUp() }) {
                                Text("‹ ${strings.get("back")}")
                            }
                        }
                    },
                    actions = {
                        TextButton(onClick = viewModel::toggleLanguage) {
                            Text(strings.get("switch_language"), fontWeight = FontWeight.SemiBold)
                        }
                    },
                )
            },
            content = { padding ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(bottom = WindowInsets.navigationBars.getBottom(androidx.compose.ui.platform.LocalDensity.current).dp)
                ) {
                    if (!online) {
                        NoticeBanner(strings.get("offline_notice"), modifier = Modifier.padding(horizontal = 16.dp))
                    } else if (!configured) {
                        NoticeBanner(strings.get("configure_api_notice"), modifier = Modifier.padding(horizontal = 16.dp))
                    }
                    uiState.BIT240102_Error?.let { errorKey ->
                        AlertDialog(
                            onDismissRequest = viewModel::clearError,
                            title = { Text(strings.get("validation_error")) },
                            text = { Text(strings.get(errorKey)) },
                            confirmButton = {
                                TextButton(onClick = viewModel::clearError) {
                                    Text(strings.get("cancel"))
                                }
                            },
                        )
                    }

                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        composable("home") {
                            DishListScreen(uiState, viewModel, strings, navController)
                        }
                        composable("categories") {
                            CategoryScreen(uiState, viewModel, strings)
                        }
                        composable(
                            "detail/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType }),
                        ) { entry ->
                            val id = entry.arguments?.getLong("id") ?: 0L
                            LaunchedEffect(id) { viewModel.loadDish(id) }
                            DishDetailScreen(
                                requestedId = id,
                                uiState = uiState,
                                strings = strings,
                                onEdit = { navController.navigate("form/$id") },
                                onDelete = { viewModel.deleteDish(id) { navController.popBackStack("home", false) } },
                                onSelectThumbnail = viewModel::chooseThumbnail,
                            )
                        }
                        composable("form/new") {
                            DishFormScreen(
                                id = null,
                                uiState = uiState,
                                viewModel = viewModel,
                                strings = strings,
                                onCancel = { navController.navigateUp() },
                                onSaved = { savedId -> navController.navigate("detail/$savedId") },
                            )
                        }
                        composable(
                            "form/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType }),
                        ) { entry ->
                            val id = entry.arguments?.getLong("id")
                            LaunchedEffect(id) { id?.let(viewModel::loadDish) }
                            DishFormScreen(
                                id = id,
                                uiState = uiState,
                                viewModel = viewModel,
                                strings = strings,
                                onCancel = { navController.navigateUp() },
                                onSaved = { navController.popBackStack() },
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (destination == "home") {
                    Button(
                        onClick = {
                            viewModel.clearError()
                            navController.navigate("form/new")
                        },
                    ) {
                        Text("+  ${strings.get("add_dish")}")
                    }
                }
            },
        )
    }
}

@Composable
private fun DishListScreen(
    state: FoodManagerUiState,
    viewModel: FoodManagerViewModel,
    strings: AppStrings,
    navController: androidx.navigation.NavHostController,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            start = 16.dp,
            top = 12.dp,
            end = 16.dp,
            bottom = 88.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.BIT240102_Filter.BIT240102_Query,
                    onValueChange = viewModel::updateQuery,
                    label = { Text(strings.get("search_hint")) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    leadingIcon = { Text("⌕") },
                )
                OutlinedButton(
                    onClick = { navController.navigate("categories") },
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    Text(strings.get("categories_title"))
                }
            }
        }
        item {
            FilterDropdown(
                title = strings.get("category_filter"),
                options = listOf(null to strings.get("all_categories")) +
                    state.BIT240102_Categories.map { it.BIT240102_Id to categoryName(it, strings) },
                selected = state.BIT240102_Filter.BIT240102_CategoryId,
                onSelect = viewModel::updateCategory,
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(strings.get("availability_filter"), style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        0 to strings.get("all_statuses"),
                        1 to strings.get("available_only"),
                        2 to strings.get("unavailable_only"),
                    ).forEach { (value, label) ->
                        if (state.BIT240102_Filter.BIT240102_Availability == value) {
                            Button(onClick = { viewModel.updateAvailability(value) }) { Text(label) }
                        } else {
                            OutlinedButton(onClick = { viewModel.updateAvailability(value) }) { Text(label) }
                        }
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = state.BIT240102_MinPriceInput,
                    onValueChange = { viewModel.updatePriceRange(it, state.BIT240102_MaxPriceInput) },
                    label = { Text(strings.get("min_price")) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = state.BIT240102_MaxPriceInput,
                    onValueChange = { viewModel.updatePriceRange(state.BIT240102_MinPriceInput, it) },
                    label = { Text(strings.get("max_price")) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                )
            }
            state.BIT240102_FilterError?.let {
                Text(
                    strings.get(it),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        item {
            FilterDropdown(
                title = strings.get("sort_by"),
                options = listOf(
                    DishSort.NAME to strings.get("sort_name"),
                    DishSort.PRICE_ASC to strings.get("sort_price_asc"),
                    DishSort.PRICE_DESC to strings.get("sort_price_desc"),
                    DishSort.PREPARATION_ASC to strings.get("sort_time_asc"),
                ),
                selected = state.BIT240102_Filter.BIT240102_Sort,
                onSelect = viewModel::updateSort,
            )
        }
        if (state.BIT240102_Dishes.isEmpty()) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Text(
                        strings.get("no_dishes"),
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        } else {
            items(state.BIT240102_Dishes, key = { it.BIT240102_Dish.BIT240102_Id }) { dish ->
                DishCard(
                    dish = dish,
                    strings = strings,
                    onClick = {
                        viewModel.loadDish(dish.BIT240102_Dish.BIT240102_Id)
                        navController.navigate("detail/${dish.BIT240102_Dish.BIT240102_Id}")
                    },
                )
            }
        }
    }
}

@Composable
private fun DishCard(
    dish: DishWithDetails,
    strings: AppStrings,
    onClick: () -> Unit,
) {
    val record = dish.BIT240102_Dish
    val thumbnail = dish.BIT240102_Images.firstOrNull { it.BIT240102_IsThumbnail }
        ?: dish.BIT240102_Images.firstOrNull()
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (thumbnail != null) {
                AsyncImage(
                    model = thumbnail.BIT240102_ImageUrl,
                    contentDescription = record.BIT240102_Name,
                    modifier = Modifier.size(92.dp).clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop,
                )
            } else {
                ImagePlaceholder(Modifier.size(92.dp))
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(record.BIT240102_Name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(categoryName(dish.BIT240102_Category, strings), style = MaterialTheme.typography.bodyMedium)
                Text(
                    strings.format("price_format", record.BIT240102_Price),
                    style = MaterialTheme.typography.titleSmall,
                    color = BIT240102_Green,
                    fontWeight = FontWeight.SemiBold,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AssistChip(
                        onClick = onClick,
                        label = { Text(strings.get(if (record.BIT240102_PreparationTime <= 15) "quick" else "regular")) },
                    )
                    AssistChip(
                        onClick = onClick,
                        label = { Text(strings.get(if (record.BIT240102_IsAvailable) "available" else "unavailable")) },
                    )
                }
                Text(
                    strings.minutes(record.BIT240102_PreparationTime),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun DishDetailScreen(
    requestedId: Long,
    uiState: FoodManagerUiState,
    strings: AppStrings,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSelectThumbnail: (Long, Long) -> Unit,
) {
    var confirmDelete by rememberSaveable { mutableStateOf(false) }
    val dishWithDetails = uiState.BIT240102_SelectedDish
        ?.takeIf { it.BIT240102_Dish.BIT240102_Id == requestedId }
    if (dishWithDetails == null) {
        EmptyPage(strings.get("dish_not_found"))
        return
    }
    val dish = dishWithDetails.BIT240102_Dish
    val images = dishWithDetails.BIT240102_Images
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Card {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(dish.BIT240102_Name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(categoryName(dishWithDetails.BIT240102_Category, strings), style = MaterialTheme.typography.titleMedium)
                    Text(strings.format("price_detail", strings.format("price_format", dish.BIT240102_Price)))
                    Text(strings.format("preparation_detail", dish.BIT240102_PreparationTime))
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(strings.get(if (dish.BIT240102_PreparationTime <= 15) "quick_label" else "regular_label"))
                        },
                    )
                    AssistChip(
                        onClick = {},
                        label = { Text(strings.get(if (dish.BIT240102_IsAvailable) "available" else "unavailable")) },
                    )
                    if (dish.BIT240102_Description.isNotBlank()) Text(dish.BIT240102_Description)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = onEdit) { Text(strings.get("edit")) }
                        OutlinedButton(onClick = { confirmDelete = true }) { Text(strings.get("delete")) }
                    }
                }
            }
        }
        if (images.isEmpty()) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Text(strings.get("no_images"), Modifier.padding(18.dp))
                }
            }
        } else {
            item { Text(strings.get("images_label"), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) }
            items(images, key = { it.BIT240102_Id }) { image ->
                DishImageCard(
                    image = image,
                    strings = strings,
                    onSelect = { onSelectThumbnail(requestedId, image.BIT240102_Id) },
                )
            }
        }
    }
    if (confirmDelete) {
        ConfirmDialog(
            title = strings.get("delete_dish_title"),
            message = strings.get("delete_dish_message"),
            strings = strings,
            onConfirm = {
                confirmDelete = false
                onDelete()
            },
            onDismiss = { confirmDelete = false },
        )
    }
}

@Composable
private fun DishImageCard(image: DishImage, strings: AppStrings, onSelect: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AsyncImage(
                model = image.BIT240102_ImageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )
            Text(image.BIT240102_ImageUrl, style = MaterialTheme.typography.bodySmall)
            if (image.BIT240102_IsThumbnail) {
                AssistChip(onClick = {}, label = { Text(strings.get("thumbnail")) })
            } else {
                OutlinedButton(onClick = onSelect) { Text(strings.get("set_thumbnail")) }
            }
        }
    }
}

@Composable
private fun DishFormScreen(
    id: Long?,
    uiState: FoodManagerUiState,
    viewModel: FoodManagerViewModel,
    strings: AppStrings,
    onCancel: () -> Unit,
    onSaved: (Long) -> Unit,
) {
    val loadedDish = uiState.BIT240102_SelectedDish
        ?.takeIf { it.BIT240102_Dish.BIT240102_Id == id }
    if (id != null && loadedDish == null) {
        LaunchedEffect(id) { viewModel.loadDish(id) }
        EmptyPage(strings.get("dish_not_found"))
        return
    }
    val existing = loadedDish?.BIT240102_Dish
    var name by rememberSaveable(id) { mutableStateOf(existing?.BIT240102_Name.orEmpty()) }
    var price by rememberSaveable(id) { mutableStateOf(existing?.BIT240102_Price?.toString().orEmpty()) }
    var time by rememberSaveable(id) { mutableStateOf(existing?.BIT240102_PreparationTime?.toString().orEmpty()) }
    var categoryId by rememberSaveable(id) { mutableStateOf(existing?.BIT240102_DishCategoryId) }
    var description by rememberSaveable(id) { mutableStateOf(existing?.BIT240102_Description.orEmpty()) }
    var images by rememberSaveable(id) {
        mutableStateOf(
            loadedDish?.BIT240102_Images.orEmpty()
                .sortedByDescending { it.BIT240102_IsThumbnail }
                .joinToString("\n") { it.BIT240102_ImageUrl },
        )
    }
    var available by rememberSaveable(id) { mutableStateOf(existing?.BIT240102_IsAvailable ?: true) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            if (uiState.BIT240102_Categories.isEmpty()) {
                Text(strings.get("error_category_required"), color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(strings.get("name_label")) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        }
        item {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text(strings.get("price_label")) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
            )
        }
        item {
            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text(strings.get("time_label")) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )
        }
        item {
            FilterDropdown(
                title = strings.get("select_category"),
                options = uiState.BIT240102_Categories.map { it.BIT240102_Id to categoryName(it, strings) },
                selected = categoryId,
                onSelect = { categoryId = it },
            )
        }
        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(strings.get("description_label")) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )
        }
        item {
            OutlinedTextField(
                value = images,
                onValueChange = { images = it },
                label = { Text(strings.get("images_label")) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )
            Text(strings.get("images_help"), style = MaterialTheme.typography.bodySmall)
        }
        item {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(strings.get("available_toggle"))
                Switch(checked = available, onCheckedChange = { available = it })
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        viewModel.saveDish(
                            id,
                            name,
                            price,
                            time,
                            categoryId,
                            available,
                            description,
                            images,
                            onSaved,
                        )
                    },
                    enabled = !uiState.BIT240102_Saving,
                ) { Text(strings.get("save")) }
                OutlinedButton(onClick = onCancel) { Text(strings.get("cancel")) }
            }
        }
    }
}

@Composable
private fun CategoryScreen(
    state: FoodManagerUiState,
    viewModel: FoodManagerViewModel,
    strings: AppStrings,
) {
    var categoryToEdit by remember { mutableStateOf<DishCategory?>(null) }
    var addingCategory by rememberSaveable { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<DishCategory?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Button(onClick = { addingCategory = true; categoryToEdit = null }) {
                Text("+  ${strings.get("add_category")}")
            }
        }
        if (state.BIT240102_Categories.isEmpty()) {
            item { Text(strings.get("no_categories")) }
        } else {
            items(state.BIT240102_Categories, key = { it.BIT240102_Id }) { category ->
                Card(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(categoryName(category, strings), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        if (category.BIT240102_Description.isNotBlank()) Text(category.BIT240102_Description)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { categoryToEdit = category; addingCategory = true }) {
                                Text(strings.get("edit"))
                            }
                            OutlinedButton(onClick = { categoryToDelete = category }) {
                                Text(strings.get("delete"))
                            }
                        }
                    }
                }
            }
        }
    }

    if (addingCategory) {
        CategoryEditor(
            category = categoryToEdit,
            strings = strings,
            onDismiss = { addingCategory = false; categoryToEdit = null },
            onSave = { name, description ->
                viewModel.saveCategory(categoryToEdit?.BIT240102_Id, name, description) {
                    addingCategory = false
                    categoryToEdit = null
                }
            },
        )
    }
    categoryToDelete?.let { category ->
        ConfirmDialog(
            title = strings.get("delete_category_title"),
            message = strings.get("delete_category_message"),
            strings = strings,
            onConfirm = {
                categoryToDelete = null
                viewModel.deleteCategory(category.BIT240102_Id)
            },
            onDismiss = { categoryToDelete = null },
        )
    }
}

@Composable
private fun CategoryEditor(
    category: DishCategory?,
    strings: AppStrings,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
) {
    var name by remember(category?.BIT240102_Id) { mutableStateOf(category?.BIT240102_Name.orEmpty()) }
    var description by remember(category?.BIT240102_Id) {
        mutableStateOf(category?.BIT240102_Description.orEmpty())
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.get(if (category == null) "add_category" else "edit_category")) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(strings.get("category_label")) },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(strings.get("category_description_label")) },
                    minLines = 2,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, description) }) { Text(strings.get("save")) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.get("cancel")) }
        },
    )
}

@Composable
private fun <T> FilterDropdown(
    title: String,
    options: List<Pair<T, String>>,
    selected: T?,
    onSelect: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val label = options.firstOrNull { it.first == selected }?.second ?: title
    Column {
        Text(title, style = MaterialTheme.typography.labelLarge)
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(label, modifier = Modifier.fillMaxWidth())
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { (value, text) ->
                    DropdownMenuItem(
                        text = { Text(text) },
                        onClick = { onSelect(value); expanded = false },
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmDialog(
    title: String,
    message: String,
    strings: AppStrings,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(strings.get("delete_confirm")) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.get("cancel")) }
        },
    )
}

@Composable
private fun NoticeBanner(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = androidx.compose.ui.graphics.Color(0xFFE9F3EA),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier.clip(RoundedCornerShape(14.dp)).background(androidx.compose.ui.graphics.Color(0xFFE7ECE6)),
        contentAlignment = Alignment.Center,
    ) {
        Text("🍽", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
private fun EmptyPage(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(message, modifier = Modifier.padding(24.dp))
    }
}

private fun categoryName(category: DishCategory, strings: AppStrings): String =
    when (category.BIT240102_Id) {
        1L -> if (category.BIT240102_Name == "Main Courses") strings.get("main_courses") else category.BIT240102_Name
        2L -> if (category.BIT240102_Name == "Appetizers") strings.get("appetizers") else category.BIT240102_Name
        3L -> if (category.BIT240102_Name == "Desserts") strings.get("desserts") else category.BIT240102_Name
        else -> category.BIT240102_Name
    }

private fun isConnected(context: Context): Boolean {
    val manager = context.getSystemService(ConnectivityManager::class.java)
    val network = manager.activeNetwork ?: return false
    val capabilities = manager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}
