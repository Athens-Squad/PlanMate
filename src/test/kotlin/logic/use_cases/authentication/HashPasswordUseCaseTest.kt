package logic.use_cases.authentication

import com.google.common.truth.Truth.assertThat
import net.thechance.logic.use_cases.authentication.HashPasswordUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HashPasswordUseCaseTest {
    private lateinit var hashPasswordUseCase: HashPasswordUseCase

    @BeforeEach
    fun setup(){
        hashPasswordUseCase = HashPasswordUseCase()
    }

    @Test
    fun `should not produce the same result as the original password when hashed`(){
        val plainPassword = PASSWORD

        val result = hashPasswordUseCase.execute(plainPassword)

        assertThat(result).isNotEqualTo(plainPassword)
    }

    @Test
    fun `should hash the password correctly for any input`(){
        val plainPassword = PASSWORD
        val hashedPassword = "d54b609242c7d758f6daca654bda1d26"

        val result = hashPasswordUseCase.execute(plainPassword)

        assertThat(result).isEqualTo(hashedPassword)
    }

    @Test
    fun `should hash the password correctly for each time the same input is given`(){
        val plainPassword = PASSWORD

        val firstResult = hashPasswordUseCase.execute(plainPassword)
        val secondResult = hashPasswordUseCase.execute(plainPassword)

        assertThat(firstResult).isEqualTo(secondResult)
    }

    @Test
    fun `should produce different hashed passwords for different inputs `(){
        val firstPlainPassword = PASSWORD
        val secondPlainPassword = "Password123"

        val firstResult = hashPasswordUseCase.execute(firstPlainPassword)
        val secondResult = hashPasswordUseCase.execute(secondPlainPassword)

        assertThat(firstResult).isNotEqualTo(secondResult)
    }

    @Test
    fun `should produce a valid hashed password for empty input`(){
        val emptyPassword = ""
        val hashedPassword = "d41d8cd98f00b204e9800998ecf8427e"

        val result = hashPasswordUseCase.execute(emptyPassword)

        assertThat(result).isEqualTo(hashedPassword)
    }
    
    companion object{
        const val PASSWORD = "123Password"
    }

}


