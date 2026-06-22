---
title: Referência da Integração Cookies KMP
nav_order: 2
permalink: /pt-br/
---

# Referência da Integração Cookies KMP

Esta página documenta a migração de regras determinísticas de cookies para uma biblioteca Kotlin Multiplatform e a integração dessa biblioteca nos forks Android e Apple do DuckDuckGo.

O objetivo é servir como material de referência para o texto do projeto: o que foi construído, por que a arquitetura foi dividida dessa forma, quais commits pertencem a cada repositório e como a implementação foi validada.

English version: [Cookies KMP Integration Reference]({{ "/" | relative_url }}).

## Repositórios

| Repositório | Papel no projeto | Branch usada |
| --- | --- | --- |
| [FernandaFBMarques/Cookies-KMP](https://github.com/FernandaFBMarques/Cookies-KMP) | Biblioteca Kotlin Multiplatform compartilhada | `main` |
| [FernandaFBMarques/Android](https://github.com/FernandaFBMarques/Android) | Fork Android que consome a biblioteca | `cookies-kmp-integration` |
| [FernandaFBMarques/apple-browsers](https://github.com/FernandaFBMarques/apple-browsers) | Fork Apple que consome a biblioteca | `cookies-kmp-integrate` |

## Visão Geral

O projeto extrai regras de negócio determinísticas de cookies do código específico de cada plataforma para um módulo Kotlin Multiplatform compartilhado.

A biblioteca compartilhada é publicada como:

```text
io.github.fernandafbmarques:cookies-kmp-core:0.1.4
```

Para Apple, a mesma biblioteca é consumida como um binary target Swift Package:

```swift
.binaryTarget(
    name: "CookiesKMP",
    url: "https://github.com/FernandaFBMarques/Cookies-KMP/releases/download/0.1.4/CookiesKMP.xcframework.zip",
    checksum: "0f22bb204cc7e0b26d01ce91d4be668db2df50aab327aed8072985032ecd0b0e"
)
```

## Arquitetura

A integração usa uma fronteira baseada em adapters.

A KMP é responsável por lógica determinística:

- decisões de feature toggle para cookies
- matching de nomes de cookies
- matching de domínios de cookies
- formatação da configuração de cookies para content-scope scripts
- fixtures de paridade entre plataformas

Android e Apple continuam responsáveis por detalhes nativos:

- repositórios e armazenamento
- Room, Core Data, UserDefaults e persistência nativa
- APIs de WebView, WebKit e cookie store
- injeção de dependência e ciclo de vida da aplicação
- leitura nativa de privacy config e feature flags
- normalização TLD/eTLD+1 quando esse comportamento já pertence ao host

Os aplicativos host coletam entradas nativas, convertem essas entradas para DTOs da KMP, chamam `CookiesHostParityFacade` e usam o resultado.

```text
Aplicativo Android / Apple
        |
        | repositórios, configs, WebKit/WebView e APIs nativas
        v
Adapter fino de plataforma
        |
        | ToggleEvaluationInput / CookieMatchInput / ConfigFormattingInput
        v
CookiesHostParityFacade
        |
        v
Regras determinísticas compartilhadas na KMP
```

## Histórico de Commits da Biblioteca KMP

Repositório: [FernandaFBMarques/Cookies-KMP](https://github.com/FernandaFBMarques/Cookies-KMP)

| Commit | Explicação |
| --- | --- |
| [`42d8119`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/42d8119) | Commit inicial do repositório. |
| [`552253b`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/552253b) | Adiciona o primeiro README do projeto Cookies KMP. |
| [`6529bd8`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/6529bd8) | Adiciona configuração inicial gerada pelo scaffold Kotlin Multiplatform. |
| [`607b481`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/607b481) | Continua a configuração inicial do projeto. |
| [`d5aae6f`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/d5aae6f) | Adiciona a primeira implementação do core de cookies em KMP e atualiza a documentação. Introduz modelos, contratos, avaliação de toggles, formatação de config, matching de nomes de cookies, redator de stacktrace e testes comuns. |
| [`5220b06`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/5220b06) | Configura publicação no Maven Central via GitHub Actions. |
| [`2a22ea6`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/2a22ea6) | Atualiza logs e visibilidade do fluxo de publicação. |
| [`cf21c0b`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/cf21c0b) | Remove import inválido de `kotlin.text.insert`. |
| [`922af04`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/922af04) | Ajusta chaves de configuração do workflow. |
| [`91c60fd`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/91c60fd) | Ajusta valores das chaves no workflow. |
| [`1f92ca1`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/1f92ca1) | Ajusta valores de versão na configuração. |
| [`24136a2`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/24136a2) | Prepara a release `0.1.0` para o Maven Central. |
| [`06cc46e`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/06cc46e) | Troca as coordenadas Maven para o namespace próprio `io.github.fernandafbmarques` e prepara a release `0.1.1`. |
| [`e4b34f5`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/e4b34f5) | Atualiza documentação Markdown depois da publicação da biblioteca. |
| [`2783255`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/2783255) | Atualiza a versão para `0.1.2`. |
| [`3efd558`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/3efd558) | Atualiza a versão para `0.1.3`. |
| [`3777c6c`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/3777c6c) | Atualiza URL e checksum do manifesto Swift Package para a release Apple `0.1.3`. |
| [`3051ccb`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/3051ccb) | Expõe `CookiesHostParityFacade` e prepara a release `0.1.4`. Adiciona fixtures de paridade, scripts/workflows para o binário Apple, contratos de adapter, testes do policy engine e a fachada pública consumida por Android e Apple. |
| [`355d6e5`](https://github.com/FernandaFBMarques/Cookies-KMP/commit/355d6e5) | Adiciona o site de documentação em GitHub Pages. |

## Commits da Integração Android

Repositório: [FernandaFBMarques/Android](https://github.com/FernandaFBMarques/Android)

Branch: [`cookies-kmp-integration`](https://github.com/FernandaFBMarques/Android/tree/cookies-kmp-integration)

### [`a44371fef`](https://github.com/FernandaFBMarques/Android/commit/a44371fef) - Delegar avaliação de toggle de cookies para KMP

Este commit adiciona a dependência KMP nos módulos de cookies do Android e migra a decisão final de toggle para a lógica compartilhada.

Arquivos alterados:

- `cookies/cookies-impl/build.gradle`
- `cookies/cookies-store/build.gradle`
- `CookiesFeatureTogglesPlugin.kt`
- `CookiesHostParityFacadeIntegrationTest.kt`

Depois da mudança:

- Android continua resolvendo o `CookiesFeatureName` nativo.
- Android continua lendo estado de toggle e versão mínima via `CookiesFeatureToggleRepository`.
- Android continua lendo a versão do app via `AppBuildConfig`.
- A KMP recebe um `ToggleEvaluationInput`.
- A KMP retorna a decisão final por `CookiesHostParityFacade.evaluateToggle(...)`.

### [`3ac92673f`](https://github.com/FernandaFBMarques/Android/commit/3ac92673f) - Delegar matching de nomes de cookies Android para KMP

Este commit migra a decisão de matching de nomes de cookies de terceiros para KMP.

Arquivos alterados:

- `RealThirdPartyCookieNames.kt`
- `RealThirdPartyCookieNamesTest.kt`

Depois da mudança:

- Android continua lendo nomes de cookies excluídos via `CookiesRepository`.
- Android converte valores nativos para `CookieMatchInput`.
- A KMP executa o matching determinístico.
- Android retorna `hasExcludedCookieName` a partir do resultado da KMP.

### [`4b3dbe444`](https://github.com/FernandaFBMarques/Android/commit/4b3dbe444) - Limpar dependências do cookies-store

Este commit finaliza a fronteira de módulos no Android para a fatia de cookies.

Arquivo alterado:

- `cookies/cookies-store/build.gradle`

Depois da mudança:

- `cookies-store` não depende mais de `cookies-kmp-core`.
- `cookies-store` continua sendo um módulo nativo Android de storage e infraestrutura.
- `cookies-store` passa a incluir a dependência `AndroidX.room.ktx`, necessária para suporte nativo ao Room.
- O consumo da lógica de negócio da KMP permanece apenas em `cookies-impl`.

Essa limpeza faz o grafo de dependências refletir a arquitetura: decisões compartilhadas de cookies ficam na camada adapter, enquanto armazenamento permanece nativo.

## Commits da Integração Apple

Repositório: [FernandaFBMarques/apple-browsers](https://github.com/FernandaFBMarques/apple-browsers)

Branch: [`cookies-kmp-integrate`](https://github.com/FernandaFBMarques/apple-browsers/tree/cookies-kmp-integrate)

### [`ccda915659`](https://github.com/FernandaFBMarques/apple-browsers/commit/ccda915659) - Integrar CookiesKMP ao BrowserServicesKit

Adiciona o binary target `CookiesKMP` ao `SharedPackages/BrowserServicesKit/Package.swift`.

O pacote passa a disponibilizar a KMP para:

- `BrowserServicesKit`
- `BrowserServicesKitTests`

### [`3786b2eee7`](https://github.com/FernandaFBMarques/apple-browsers/commit/3786b2eee7) - Avaliar configuração de cookies via KMP no Apple

Migra a decisão de injeção e formatação da configuração de cookies do content-scope script para KMP.

Arquivos alterados:

- `ContentScopePrivacyConfigurationJSONGenerator.swift`
- `ContentScopePrivacyConfigurationJSONGeneratorTests.swift`
- `CookiesHostParityFacadeTests.swift`

Depois da mudança:

- Apple continua carregando a privacy configuration nativa.
- Apple continua resolvendo estado de usuário interno via `featureFlagger.internalUserDecider`.
- Apple converte o estado nativo para `ToggleEvaluationInput`.
- KMP decide se a configuração de cookies deve ser injetada.
- KMP formata o fragmento de configuração do content-scope script.

### [`021770a514`](https://github.com/FernandaFBMarques/apple-browsers/commit/021770a514) - Delegar matching de domínio Apple para KMP

Adiciona `CookiesDomainMatcher`, um adapter pequeno em Apple ao redor de `CookiesHostParityFacade.evaluateCookie(...)`.

Arquivos alterados:

- `CookiesDomainMatcher.swift`
- `CookiesDomainMatcherTests.swift`
- `iOS/Core/HTTPCookieExtension.swift`

Depois da mudança:

- iOS mantém WebKit e acesso a cookies nativos.
- iOS passa os domínios para `CookiesDomainMatcher`.
- KMP executa o matching determinístico.

### [`cf9e76d075`](https://github.com/FernandaFBMarques/apple-browsers/commit/cf9e76d075) - Delegar matching de domínio macOS para KMP

Estende a integração Apple para os helpers de domínio de cookies no macOS.

Arquivos alterados:

- `macOS/DuckDuckGo/Common/Extensions/HTTPCookie.swift`
- `macOS/DuckDuckGo/Fireproofing/Model/FireproofDomains.swift`

Depois da mudança:

- macOS continua responsável por storage de fireproofing e normalização TLD/eTLD+1.
- macOS converte domínios nativos para a forma esperada pelo matcher compartilhado.
- KMP executa a decisão final de matching.

### [`da812285be`](https://github.com/FernandaFBMarques/apple-browsers/commit/da812285beb728834c0b52323619d74e9ea5b97d) - Encaminhar a formatação Apple pela fachada KMP

Essa limpeza arquitetural atualiza `ContentScopePrivacyConfigurationJSONGenerator` para acessar a formatação compartilhada por meio da fachada pública destinada aos hosts, em vez de chamar `CookiesContentScopeConfigFormatter` diretamente.

Arquivo alterado:

- `SharedPackages/BrowserServicesKit/Sources/BrowserServicesKit/ContentScopeScript/ContentScopePrivacyConfigurationJSONGenerator.swift`

Depois da mudança:

- Apple continua responsável por carregar, filtrar, serializar e aplicar a privacy configuration nativa.
- Apple converte o JSON serializado para `ConfigFormattingInput`.
- `CookiesHostParityFacade.formatConfig(...)` delega a formatação para `CookiesPolicyEngine` e o formatter compartilhado.
- O comportamento do JSON gerado permanece inalterado, enquanto o host Apple passa a respeitar a mesma fronteira pública KMP usada pelas demais integrações.

## Testes e Validação

### Biblioteca KMP

A biblioteca contém testes compartilhados para:

- `CookiesPolicyEngineTest`
- `CrossPlatformParityFixturesTest`
- `CookiesRepositoryAdaptersTest`
- `CookiesFeatureStoreUpdaterTest`
- `StacktraceRedactorTest`

Comando relevante:

```bash
./gradlew :shared:testDebugUnitTest
```

### Android

Testes focados que passaram:

```bash
./gradlew :cookies-impl:testDebugUnitTest \
  --tests com.duckduckgo.cookies.impl.features.CookiesFeatureTogglesPluginTest \
  --tests com.duckduckgo.cookies.impl.thirdpartycookienames.RealThirdPartyCookieNamesTest \
  --tests com.duckduckgo.cookies.impl.CookiesHostParityFacadeIntegrationTest \
  --no-daemon
```

Esses testes validam:

- delegação da avaliação de toggle para KMP
- delegação do matching de nomes de cookies para KMP
- fixtures de paridade entre Android e KMP
- manutenção das fronteiras nativas de storage/repositórios

### Apple

Testes Swift Package que passaram:

```bash
swift test --package-path SharedPackages/BrowserServicesKit \
  --filter 'CookiesHostParityFacadeTests|CookiesDomainMatcherTests|ContentScopePrivacyConfigurationJSONGeneratorTests'
```

Resultado:

```text
Executed 12 tests, with 0 failures
```

Esses testes validam:

- avaliação de toggle do content-scope de cookies via KMP
- formatação da configuração de cookies via KMP
- matching de domínios iOS via KMP
- matching de domínios macOS via KMP
- consumo das fixtures de paridade da KMP a partir de Swift

A validação completa via Xcode no macOS foi tentada, mas ficou bloqueada por problemas de resolução de dependências existentes e não relacionados à migração de cookies:

- acesso ao repositório privado `native-apps-ducksans`
- mapeamento do binary target `BloomFilter`
- mapeamento do binary target `URLPredictorRust`

## Decisões de Fronteira

A KMP não substitui infraestrutura nativa. Ela não assume:

- repositórios Android
- storage Room
- storage Apple em UserDefaults/Core Data
- APIs WebView ou WebKit
- ciclo de vida do app
- injeção de dependência
- wiring de runtime ou release

Ela assume apenas regras portáveis e determinísticas.

Por isso, vários arquivos nativos continuam existindo depois da migração. Eles são adapters, repositórios ou pontos de integração de plataforma. O objetivo não é transformar o app inteiro em código compartilhado. O objetivo é ter uma fonte única para decisões de política de cookies.
