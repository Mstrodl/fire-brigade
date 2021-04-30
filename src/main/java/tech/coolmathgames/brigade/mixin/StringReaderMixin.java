package tech.coolmathgames.brigade.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = StringReader.class, remap = false)
public class StringReaderMixin {
	private static final char SYNTAX_ESCAPE = '\\';

	/**
	 * @author Mary Strodl <ipadlover8322@gmail.com>
	 * @reason Backport of https://github.com/Mojang/brigadier/pull/90
	 */
	@Overwrite(remap = false)
	public String readStringUntil(char terminator) throws CommandSyntaxException {
		StringReader self = (StringReader)(Object)this;
		final StringBuilder result = new StringBuilder();
		boolean escaped = false;
		while (self.canRead()) {
			final char c = self.read();
			if (escaped) {
				Character mogrified = null;
				if (c == terminator) {
					mogrified = terminator;
				} else {
					switch (c) {
						case SYNTAX_ESCAPE:
							mogrified = SYNTAX_ESCAPE;
							break;
						case '/':
							mogrified = '/';
							break;
						case 'b':
							mogrified = '\b';
							break;
						case 'f':
							mogrified = '\f';
							break;
						case 'n':
							mogrified = '\n';
							break;
						case 'r':
							mogrified = '\r';
							break;
						case 't':
							mogrified = '\t';
							break;
					}
				}
				if(mogrified != null) {
					result.append(mogrified);
					escaped = false;
				} else {
					self.setCursor(self.getCursor() - 1);
					throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(self, String.valueOf(c));
				}
			} else if (c == SYNTAX_ESCAPE) {
				escaped = true;
			} else if (c == terminator) {
				return result.toString();
			} else {
				result.append(c);
			}
		}

		throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedEndOfQuote().createWithContext(self);
	}
}
