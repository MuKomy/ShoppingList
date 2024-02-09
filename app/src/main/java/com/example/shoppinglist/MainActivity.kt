package com.example.shoppinglist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.ShoppingListTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                //surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                ShoppingList()
                }
            }
        }
    }
}



@Composable
fun ShoppingList() {

    val context = LocalContext.current
    var sItems by remember { mutableStateOf(listOf<ShoppingItems>()) }
    var showAlertDialog by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    var quantityValue by remember { mutableStateOf("") }
    var showEditingDialog  by remember { mutableStateOf(false) }
    var quantityValueToInt = quantityValue.toIntOrNull() ?:0
    var editingValues by remember { mutableStateOf( ShoppingItems( 0, "editingValues", 0, false ))}




    @Composable
    fun EditShoppingItem(
        itemEditing: ShoppingItems,
        onUpdate: (ShoppingItems) -> Unit,
        onDelete: () -> Unit
    ) {
        var editingName by remember { mutableStateOf(itemEditing.name) }
        var editingQuantityString by remember { mutableStateOf(itemEditing.quantityString) }

        AlertDialog(
            onDismissRequest = { showEditingDialog = false },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = { showEditingDialog = false },
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = {
                            if (editingName.isNotBlank() && editingQuantityString.isNotBlank()) {
                                val editedItem = ShoppingItems(
                                    id = itemEditing.id,
                                    name = editingName,
                                    quantity = editingQuantityString.toInt()
                                )
                                onUpdate(editedItem)
                                showEditingDialog = false
                            } else {
                                Toast.makeText( context,"Please Enter Valid Data" ,Toast.LENGTH_SHORT ).show()
                            }
                        },
                    ) {
                        Text(text = "Update")
                    }
                    
                }
            },
            title = { Text(text = "Edit Item") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = editingName,
                        onValueChange = { editingName = it },
                        label = { Text("Name") }
                    )
                    OutlinedTextField(
                        singleLine = true,
                        value = editingQuantityString,
                        onValueChange = { editingQuantityString = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text("Quantity") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        )
    }




    if(showAlertDialog){
        AlertDialog(

            onDismissRequest = {showAlertDialog = false},
            confirmButton = { Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement =Arrangement.SpaceBetween) {

                Button(
                    onClick = { showAlertDialog = false },
                    //modifier = Modifier.align(Alignment.CenterStart)
                )
                {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = {
                        if(textValue.isNotBlank() && quantityValueToInt > 0){
                            val newItem = ShoppingItems(
                                id = sItems.size +1,
                                name = textValue,
                                quantity =quantityValueToInt
                            )
                            sItems += newItem
                            textValue = ""
                            quantityValue = ""
                            showAlertDialog = false
                            Toast.makeText( context,"Added ${newItem.quantity} of ${newItem.name} to ID ${newItem.id}" ,Toast.LENGTH_SHORT ).show()
                            //Toast.makeText( context,"Worked ${quantityValue.toIntOrNull()}" ,Toast.LENGTH_SHORT ).show()
                            //Toast.makeText( context,"Worked to int value $quantityValueToInt" ,Toast.LENGTH_SHORT ).show()
                        }else{

                            Toast.makeText( context,"Please Enter Valid Data" ,Toast.LENGTH_SHORT ).show()
                            //Toast.makeText( context,"Worked to int value $quantityValueToInt" ,Toast.LENGTH_SHORT ).show()

                        }


                    },
                )
                {
                    Text(text = "Add")
                }
            }},
            title = { Text(text = "Add Item")},
            text = {
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)){
                    OutlinedTextField(

                        value = textValue,
                        onValueChange = {textValue = it},
                        label = {Text("Name") }
                    )
                    OutlinedTextField(
                        singleLine = true,
                        value = quantityValue,
                        onValueChange = {quantityValue = it},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {Text("Quantity") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        )
    }



    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.padding(8.dp),
        state = lazyListState
    ) {
        items(sItems) { item ->
            ShoppingListItem(
                item,
                {
                    editingValues = item
                    showEditingDialog = true
                },
                { sItems = sItems.filterNot { it.id == item.id } }
            )
        }
    }

    // ExtendedFloatingActionButton with AnimatedVisibility
    AnimatedVisibility(
        visible = lazyListState.firstVisibleItemIndex == 0,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
            .padding(16.dp)

    ) {Box(contentAlignment = Alignment.BottomEnd){
        ExtendedFloatingActionButton(
            onClick = { showAlertDialog = true },
            icon = { Icon(Icons.Default.Add, null) },
            text = { Text(text = "Add Items") },
            modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)
        )}
    }





    if (showEditingDialog) {
        EditShoppingItem(
            itemEditing = editingValues,
            onUpdate = { updatedItem ->
                sItems = sItems.map { if (it.id == updatedItem.id) updatedItem else it }
            },
            onDelete = {
                sItems = sItems.filterNot { it.id == editingValues.id }
                showEditingDialog = false
            }
        )
    }


}



data class ShoppingItems(val id : Int=0, var name :String="", var quantity :Int=0, var isEditing :Boolean = false) {
    var quantityString: String = quantity.toString()
}

@Preview(showBackground = true)
@Composable
fun ShoppingListPreview(){
    ShoppingList()

}

@Composable
fun ShoppingListItem(
    item: ShoppingItems,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .border(border = BorderStroke(2.dp, Color.Gray), shape = RoundedCornerShape(20.dp))
    ) {
        Text(
            item.name,
            Modifier
                .width(200.dp)
                .padding(8.dp),
            fontFamily = FontFamily.SansSerif
        )

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
        ) {
            Text("Qty: ${item.quantity}", fontFamily = FontFamily.SansSerif)
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, null)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null)
            }
        }
    }
}

