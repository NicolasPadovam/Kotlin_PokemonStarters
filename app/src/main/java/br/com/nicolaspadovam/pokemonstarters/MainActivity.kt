package br.com.nicolaspadovam.pokemonstarters

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.nicolaspadovam.pokemonstarters.ui.theme.PokemonStartersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokemonStartersTheme {
                Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                    PokemonStarterScreen()
                }
            }
        }
    }
}

val starters = listOf(
    Pokemon("Bulbassaur", R.drawable.bulbassaur),
    Pokemon("Charmander", R.drawable.charmander),
    Pokemon(imageRes = R.drawable.squirtle, nome = "Squirtle")
)

@Composable
fun PokemonStarterScreen(modifier: Modifier = Modifier) {
    var pokemonSelected by remember { mutableStateOf(starters.first()) }

    val config = LocalConfiguration.current
    val isPortrait = config.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        PokemonStarterScreenPortrait(
            modifier = modifier,
            pokemonSelected = pokemonSelected,
            onSelected = { pokemonSelected = it }
        )
    } else {
        PokemonStarterScreenLandscape(
            modifier = modifier,
            pokemonSelected = pokemonSelected,
            onSelected = { pokemonSelected = it }
        )
    }


}

@Composable
fun PokemonStarterScreenPortrait(
    modifier: Modifier = Modifier,
    pokemonSelected: Pokemon,
    onSelected: (Pokemon) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PokeLogo()
        PokemonHeader("Escolha seu Pokémon Inicial")
        Spacer(modifier = Modifier.weight(1f))
        PokeCard(pokemonSelected)
        Spacer(modifier = Modifier.weight(1f))
        PokeOptionList(
            pokemons = starters,
            pokemonSelected = pokemonSelected,
            onSelected = onSelected
        )
    }
}

@Composable
fun PokemonStarterScreenLandscape(
    modifier: Modifier = Modifier,
    pokemonSelected: Pokemon,
    onSelected: (Pokemon) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Esquerda -> Pokemon selecionado
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            PokeLogo()
            Spacer(modifier = modifier.height(32.dp))
            PokeCard(pokemon = pokemonSelected)
        }
        //Direita -> opcoes de pokemon
        Box(
            modifier = modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column {
                PokemonHeader("Escolha seu Pokemon inicial")
                Spacer(modifier = Modifier.height(32.dp))
                PokeOptionList(
                    pokemons = starters,
                    pokemonSelected = pokemonSelected,
                    onSelected = onSelected
                )
            }
        }
    }


}

@Composable
fun PokeLogo() {
    Image(
        painter = painterResource(id = R.drawable.logo_pokemon),
        contentDescription = "Pokemon Logo",
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    )
}


@Composable
fun PokemonHeader(label: String) {
    Text(
        text = label,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()

    )
}

data class Pokemon(
    val nome: String,
    val imageRes: Int = R.drawable.pokeball_unselected
)

@Composable
fun PokeCard(pokemon: Pokemon) {
    Column(horizontalAlignment = Alignment.CenterHorizontally)
    {
        Image(
            painter = painterResource(pokemon.imageRes),
            contentDescription = "Pokemon selecionado é o ${pokemon.nome}",
            modifier = Modifier.size(250.dp)
        )
        Text(
            text = pokemon.nome.uppercase(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PokeOption(
    pokemon: Pokemon,
    selected: Boolean,
    onSelected: (Pokemon) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onSelected(pokemon) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if (selected) painterResource(R.drawable.pokeball_selected)
            else painterResource(R.drawable.pokeball_unselected),
            contentDescription = pokemon.nome,
            modifier = Modifier.size(40.dp),
            colorFilter = if (isSystemInDarkTheme() && !selected) ColorFilter.tint(Color.White) else null
        )
        Text(pokemon.nome, fontSize = 20.sp, fontWeight = FontWeight.Bold)

    }

}

@Composable
fun PokeOptionList(
    pokemons: List<Pokemon>,
    pokemonSelected: Pokemon,
    onSelected: (Pokemon) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        pokemons.forEach { pokemon ->
            PokeOption(
                pokemon = pokemon,
                selected = pokemon == pokemonSelected,
                onSelected = onSelected
            )
        }
    }
}


@Preview
@Composable
private fun PokeOptionsPreview(modifier: Modifier = Modifier) {
    PokeOption(starters.first(), true) { }
}


@Preview(showBackground = true)
@Composable
private fun PokemonStarterScreenPreview() {
    PokemonStartersTheme {
        PokemonStarterScreen()
    }

}