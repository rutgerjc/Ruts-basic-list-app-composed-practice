package com.runitrut.ruts_basic_list_app_composed_practice.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Data class for ShoppingItems
data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)


@Preview(showBackground = true)
@Composable
fun ShoppingListApp(){

    // puts Data class ShoppingItem's into a mutable state of var "sItems"
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    // Dialog to add items
    var showDialog by remember { mutableStateOf(false) }
    // Text Fields
    var itemName by remember { mutableStateOf("") }
    // Text Fields
    var itemQuantity by remember { mutableStateOf("") }
    val customColor = Color(0xFF6E9B9A)


    // Top of Column / Top of app
    Column(
        // Fills max size UI, with 32dp padding
        modifier = Modifier.fillMaxSize().padding(8.dp),
        // Centers Column items, vertically in the middle of the screen.
        verticalArrangement = Arrangement.Center

    ) {
        Button( // Button to add items
            onClick = {showDialog = true},
            modifier = Modifier
                .align(Alignment.CenterHorizontally).padding(top = 48.dp)
            , colors = ButtonDefaults.buttonColors(
                containerColor = customColor,
                contentColor = Color.Black
            )

        ) {
            Text("Add Item")
        }
        LazyColumn( // Lazy Column to display list items
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Holds the contents of the LAzt Column
            items(sItems){
                item ->
                if(item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete = {
                        editedName, editedQuantity ->
                        sItems = sItems.map{it.copy(isEditing = false)}
                        val editedItem = sItems.find{it.id== item.id}
                        editedItem?.let{
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                }else {
                    ShoppingListItem(item = item , onEditClick = {
                        // finding out which item to edit
                        sItems = sItems.map{it.copy(isEditing = it.id==item.id)}
                    },
                        onDeleteClick = {
                        sItems = sItems-item
                    })
                }
            }
        }
    }

    if(showDialog){
        AlertDialog(onDismissRequest = {  showDialog = false  },
            confirmButton = {
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Button(
                                    modifier = Modifier, colors = ButtonDefaults.buttonColors(
                                        containerColor = customColor,
                                        contentColor = Color.Black
                                    ),
                                    onClick = {
                                    if(itemName.isNotBlank()){
                                        val newItem = ShoppingItem(
                                            id = sItems.size +1,
                                            name = itemName,
                                            quantity = itemQuantity.toInt()
                                        )
                                        sItems = sItems + newItem
                                        showDialog = false
                                        itemName = ""
                                    }
                                }){
                                    Text("Add")
                                }
                                Button(
                                    modifier = Modifier, colors = ButtonDefaults.buttonColors(
                                        containerColor = customColor,
                                        contentColor = Color.Black
                                    ),
                                    onClick = {
                                    showDialog = false
                                }) {
                                    Text("cancel")
                                }
                            }
            },
            title = { Text(text = "Add shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                }
            }
            )
    }
}


@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }
    val customColor = Color(0xFF6E9B9A)

    Row(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(customColor)
        .padding(24.dp)
        , horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column{
            OutlinedTextField(
                value = editedName,
                textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center),
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)

            )
            OutlinedTextField(
                value = editedQuantity,
                textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center),
                onValueChange = { editedQuantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
            )
        }
        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?:  1)
        }
        ){
            Text("Save")
        }

    }
}



@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick:() -> Unit,
    onDeleteClick:() -> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            ),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Text(
            text = item.name,
            modifier = Modifier.padding(8.dp).align(alignment = Alignment.CenterVertically),
            )
        Text(
            text = "Qty: ${item.quantity}",
            modifier = Modifier.padding(8.dp).align(alignment = Alignment.CenterVertically),
            )
        Row (
            modifier = Modifier.padding(8.dp)
        ){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }

        }
    }

}
